package com.googlecode.httpfilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.client.Address;
import org.eclipse.jetty.client.CachedExchange;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.EofException;
import org.eclipse.jetty.io.nio.RandomAccessFileBuffer;
import org.eclipse.jetty.util.IO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.httpfilter.handler.DataBlock;
import com.googlecode.httpfilter.handler.HttpRequestHandler;
import com.googlecode.httpfilter.handler.HttpResponseHandler;
import com.googlecode.httpfilter.handler.RequestHandlerResult;
import com.googlecode.httpfilter.handler.ResponseHandlerResult;
import com.googlecode.httpfilter.mime.MIME;
import com.googlecode.httpfilter.util.HttpUtils;

/**
 * HttpFilter��Exchange
 * @author vlinux
 *
 */
public class HttpFilterExchange extends CachedExchange {

	
	private static final Logger logger = LoggerFactory.getLogger("httpfilter");
	
	private final List<HttpRequestHandler> _httpRequestHandlers;
	private final List<HttpResponseHandler> _httpResponseHandlers;
	private final HashSet<String> _dontProxyToBrowserHeaders;
	private final HttpServletRequest _bpRequest;
	private final HttpServletResponse _bpResponse;
	private final Continuation _bpRequestContinuation;
	private final OutputStream _bpOut;
	
	private boolean _waitForCompleted = false;	//�Ƿ�ȴ�����chunk���
	private Buffer _waitForCompletedBuffer;		//���ݻ������

	public HttpFilterExchange(
			final List<HttpRequestHandler> _httpRequestHandlers,
			final List<HttpResponseHandler> _httpResponseHandlers,
			final HashSet<String> _DontProxyToBrowserHeaders,
			final HttpServletRequest bpRequest,
			final HttpServletResponse bpResponse) throws IOException {
		super(true);
		this._httpRequestHandlers = _httpRequestHandlers;
		this._httpResponseHandlers = _httpResponseHandlers;
		this._dontProxyToBrowserHeaders = _DontProxyToBrowserHeaders;
		this._bpRequest = bpRequest;
		this._bpResponse = bpResponse;
		this._bpRequestContinuation = ContinuationSupport.getContinuation(bpRequest);
		this._bpOut = bpResponse.getOutputStream();
	}
	
	@Override
	protected void onRequestComplete() throws IOException {
		super.onRequestComplete();
		try {
			for( HttpRequestHandler reqHandler : _httpRequestHandlers ) {
				if( reqHandler.isHandleRequest(this) ) {
					final RequestHandlerResult result = reqHandler.handleRequest(this);
					processProxyToServerRequestHandlerResult(result);
				}
			}
		} catch (Exception e) {
			logger.warn("handle response filter occre exception, URI={}", new Object[]{_bpRequest.getRequestURI(), e});
		}
		
	}

	@Override
	protected void onResponseHeader(Buffer name, Buffer value)
			throws IOException {
			
		final String nameStr = name.toString();
		final String valueStr = value.toString();
		
		if( StringUtils.isBlank(nameStr)
				|| StringUtils.isBlank(valueStr)) {
			return;
		}
		
		for( String dontProxyHeader : _dontProxyToBrowserHeaders ) {
			if( StringUtils.equalsIgnoreCase(nameStr, dontProxyHeader) ) {
				return;
			}
		}
		
		logger.debug(name+": (filtered): "+valueStr);
		_bpResponse.addHeader(nameStr, valueStr);
		super.onResponseHeader(name, value);
		
	}
	
	@Override
	protected void onResponseHeaderComplete() throws IOException {
		super.onResponseHeaderComplete();
		
		final String mime = HttpUtils.getMIME(getResponseFields());
		logger.debug("url={};mime={}",_bpRequest.getRequestURL(),mime);
		_waitForCompleted = MIME.isSupport(mime);
		_waitForCompletedBuffer = createBuffer(_waitForCompleted);
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResponseStatus(Buffer version, int status,
			Buffer reason) throws IOException {
		logger.debug("response status changed. URI={};status={};reason={}", new Object[]{_bpRequest.getRequestURI(), status, reason});
	
		if (reason != null && reason.length() > 0) {
			_bpResponse.setStatus(status, reason.toString());
		} else {
			_bpResponse.setStatus(status);
		}
		
		super.onResponseStatus(version, status, reason);
	}

	@Override
	protected void onResponseContent(Buffer content)
			throws IOException {
		logger.debug("content for URI={};length={}", _bpRequest.getRequestURI(), content.length());
		if( _waitForCompleted ) {
			_waitForCompletedBuffer.put(content);
		} else {
			content.writeTo(_bpOut);
		}
	}

	@Override
	protected void onResponseComplete() throws IOException {
		logger.debug("complete for URI={};", new Object[]{_bpRequest.getRequestURI()});
		try {
			if( _waitForCompleted ) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				_waitForCompletedBuffer.writeTo(baos);
				DataBlock block = new DataBlock(baos.toByteArray());
				IO.close(baos);//���ﲻ���뵽finally�������ϲ�������������²����׳��쳣
				try {
					for( HttpResponseHandler respHandler : _httpResponseHandlers ) {
						if( respHandler.isHandleResponse(this) ) {
							final ResponseHandlerResult result = respHandler.handleResponse(this, block);
							processServerToProxyResponseHandlerResult(result);
							block = result.getBlock();
						}
					}
				} catch (Exception e) {
					logger.warn("handle response filter occre exception, URI={}", new Object[]{_bpRequest.getRequestURI(), e});
				}
				try {
					_bpOut.write(block.getDatas());
				}catch(EofException e) {
					// �����ִ�ֻ������������ر�������������
					logger.warn("EofException for URL={};",_bpRequest.getRequestURL());
					logger.debug("EofException for URL={};",_bpRequest.getRequestURL(), e);
				}
			}
		} finally {
			_bpRequestContinuation.complete();
		}
	}

	@Override
	protected void onConnectionFailed(Throwable ex) {
		handleOnConnectionFailed(ex, _bpRequest, _bpResponse);

		// it is possible this might trigger before the
		// continuation.suspend()
		if (!_bpRequestContinuation.isInitial()) {
			_bpRequestContinuation.complete();
		}
	}

	@Override
	protected void onException(Throwable ex) {
		handleOnException(ex, _bpRequest, _bpResponse);

		// it is possible this might trigger before the
		// continuation.suspend()
		if (!_bpRequestContinuation.isInitial()) {
			_bpRequestContinuation.complete();
		}
	}

	@Override
	protected void onExpire() {
		handleOnExpire(_bpRequest, _bpResponse);
		_bpRequestContinuation.complete();
	}
	
	/**
	 * ����server ======+ proxy�ķ��ؽ��
	 * @param result
	 */
	private void processServerToProxyResponseHandlerResult(ResponseHandlerResult result) {
		for( Entry<String, String> e : result.getHeaderModifier().entrySet() ) {
			_bpResponse.setHeader(e.getKey(), e.getValue());
		}
		for( String key : result.getHeaderRemover() ) {
			_bpResponse.setHeader(key, null);
		}
	}

	/**
	 * ����proxy ======+ server�ķ��ؽ��
	 * @param result
	 */
	private void processProxyToServerRequestHandlerResult(RequestHandlerResult result) {
		for( Entry<String, String> e : result.getHeaderModifier().entrySet() ) {
			setRequestHeader(e.getKey(), e.getValue());
		}
		for( String key : result.getHeaderRemover() ) {
			setRequestHeader(key, null);
		}
	}

	/**
	 * ����buffer
	 * @param waitForCompleted
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private RandomAccessFileBuffer createBuffer(final boolean waitForCompleted) throws FileNotFoundException,
			IOException {
		if( !waitForCompleted ) {
			return null;
		}
		return new RandomAccessFileBuffer(File.createTempFile("__HTTPFILTER_", "temp_"+System.currentTimeMillis()));
	}

	/**
	 * Extension point for custom handling of an HttpExchange's
	 * onConnectionFailed method. The default implementation delegates to
	 * {@link #handleOnException(Throwable, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	private void handleOnConnectionFailed(Throwable ex,
			HttpServletRequest request, HttpServletResponse response) {
		handleOnException(ex, request, response);
	}

	/**
	 * Extension point for custom handling of an HttpExchange's onException
	 * method. The default implementation sets the response status to
	 * HttpServletResponse.SC_INTERNAL_SERVER_ERROR (503)
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	private void handleOnException(Throwable ex, HttpServletRequest request,
			HttpServletResponse response) {
		if(ex instanceof EofException) {
			logger.debug("EOF Error", ex);
		} else if (ex instanceof IOException) {
			logger.warn("I/O Error URI={}, MSG={}", new Object[]{request.getRequestURI(), ex.toString()});
			logger.debug("I/O Error", ex);
		} else {
			logger.warn("I/O Error", ex);
		}
			
		if (!response.isCommitted()) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Extension point for custom handling of an HttpExchange's onExpire method.
	 * The default implementation sets the response status to
	 * HttpServletResponse.SC_GATEWAY_TIMEOUT (504)
	 * 
	 * @param request
	 * @param response
	 */
	private void handleOnExpire(HttpServletRequest request,
			HttpServletResponse response) {
		if (!response.isCommitted()) {
			response.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT);
		}
	}
	
	/* ------------------------------------------------------------ */
	/**
	 * @param uri
	 *            an absolute URI (for example 'http://localhost/foo/bar?a=1')
	 * @deprecated
	 * 	please use {@link #setURL(URL)}
	 */
	public void setURI(URI uri) {
		throw new UnsupportedOperationException("setURI can't use in HttpFilterExchange. please use setURL();");
	}
	
	/**
	 * @deprecated
	 * 	please use {@link #setURL(URL)}
	 */
	public void setURL(String urlString) {
		throw new UnsupportedOperationException("setURI can't use in HttpFilterExchange. please use setURL();");
	}
	
	public void setURL(URL url) {
		final String scheme = url.getProtocol();
		int port = url.getPort();
		if (port <= 0) {
			port = "https".equalsIgnoreCase(scheme) ? 443 : 80;
		}
		setScheme(scheme);
		setAddress(new Address(url.getHost(), port));
		
		HttpURI httpUri = new HttpURI(url.toString());
		String completePath = httpUri.getCompletePath();
		setRequestURI(completePath == null ? "/" : completePath);
//		setRequestURI(StringUtils.isBlank(url.getPath()) ? "/" : url.getPath());
	}

}
