package com.googlecode.httpfilter.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.googlecode.httpfilter.constant.ErrorCodeConstants;
import com.googlecode.httpfilter.domain.CommunicationDO;
import com.googlecode.httpfilter.domain.ConnectionDO;
import com.googlecode.httpfilter.domain.ItemDO;
import com.googlecode.httpfilter.domain.LogHtmlDO;
import com.googlecode.httpfilter.domain.RuleDO;
import com.googlecode.httpfilter.domain.SingleResultDO;
import com.googlecode.httpfilter.domain.ToBeCheckDO;
import com.googlecode.httpfilter.domain.VersionDO;
import com.googlecode.httpfilter.manager.SpecialItemManager;
import com.googlecode.httpfilter.service.CommunicationService;
import com.googlecode.httpfilter.service.ConnectionService;
import com.googlecode.httpfilter.service.RuleService;
import com.googlecode.httpfilter.service.ToBeCheckService;
import com.googlecode.httpfilter.service.VersionService;

@Controller
public class ViewIndexController {

	@RequestMapping("/index.do")
	public String viewIndex() {
		return "index";
	}

	private String[] title = { "���", "URL", "״̬", "����", "�ͻ���IP" };
	private String[] comtTitle = { "���", "����" };
	private String[] itemTitle = { "����id","��������", "����" };
	private String[] ruleTitle = { "���", "�ؼ���", "У������", "��У���ֶ�" };
	private String[] logTitle = { "����ID", "TC", "���", "��ϸ���" };
	
	private String detailskipPre = "42.120.82.62 detailskip.taobao.com m.ajax.taobao.com ajax.tbcdn.cn as.taobao.com tds.alicdn.com;42.120.82.62 pre.detailskip.taobao.com pre.ajax.tbcdn.cn m.ajax.taobao.com pre.tds.alicdn.com";
	private String detailskipBeta = "110.75.96.18 detailskip.taobao.com  ajax.tbcdn.cn  m.ajax.taobao.com  as.taobao.com";
	@Autowired
	ConnectionService contService;
	@Autowired
	RuleService ruleService;
	@Autowired
	VersionService versionService;
	@Autowired
	SpecialItemManager specialItemManager;
	@Autowired
	ToBeCheckService toBeCheckService;
	@Autowired
	CommunicationService comtService;

	@RequestMapping("/con.do")
	public String contView(@RequestParam("trace_id") String traceId,
			HttpServletRequest request, ModelMap mod) throws Exception {
		mod.addAttribute("title", title);
		SingleResultDO<List<ConnectionDO>> result = contService
				.getConnectionByTraceId(traceId);
		mod.addAttribute("resultList", result.getModel());
		return "showcon";
	}

	@RequestMapping("/openurl.do")
	public String openUrlView(ModelMap mod) {
		UUID uuid = UUID.randomUUID();
		mod.addAttribute("trace_id", uuid.toString());
		return "openurl";
	}
	
	@RequestMapping("/filter.do")
	public String welcomeView(ModelMap mod) {
		
		List<RuleDO> rules = new ArrayList<RuleDO>();
		//��ȡ���еĹ���
		SingleResultDO<List<RuleDO>> result = ruleService.searchAllRules();
		if( result.isSuccess() ){
			rules = result.getModel();
			mod.addAttribute( "rules", rules );
		}else{
			mod.addAttribute( "error", result.getErrMsg().getErrorCode() );
		}
		return "filter";
	}
	
	@RequestMapping("/rulelist.do")
	public String ruleListView(ModelMap mod) {
		List<RuleDO> rules = new ArrayList<RuleDO>();
		//��ȡ���еĹ���
		SingleResultDO<List<RuleDO>> result = ruleService.searchAllRules();
		if( result.isSuccess() ){
			rules = result.getModel();
			mod.addAttribute("rules", rules);
			mod.addAttribute("title", ruleTitle);
			mod.addAttribute("isSuccess", "�ɹ�");
		}else{
			mod.addAttribute("isSuccess", "ʧ��");
		}
		return "rulelist";
	}
	
	@RequestMapping("/delrule.do")
	public String delRuleView( @RequestParam("chooseRule") String[] chooseRule, ModelMap mod ) {
		List<RuleDO> rules = new ArrayList<RuleDO>();
		if( chooseRule != null && chooseRule.length > 0 ){
			for( int index = 0; index < chooseRule.length; index ++ ){
				SingleResultDO<RuleDO>result = ruleService.delRuleById( Long.parseLong( chooseRule[index] ) );
				if( result.isSuccess() && result.getModel() != null ){
					rules.add(result.getModel());
				}
			}
			if( rules.size() == 0 ){
				mod.addAttribute("isSuccess", "ʧ��");
			}else{
				mod.addAttribute("rules", rules);
				mod.addAttribute("title", ruleTitle);
				mod.addAttribute("isSuccess", "�ɹ�");
			}
		}else{
			mod.addAttribute("isSuccess", "ʧ��");
		}
		return "delrule";
	}
	
	@RequestMapping("/createrule.do")
	public String createRuleView(ModelMap mod) {
		return "createrule";
	}

	/**
	 * ��������
	 * @param keyWords
	 * @param checkType
	 * @param exceptFields
	 * @param mod
	 * @return
	 */
	@RequestMapping(value = "/addrule.do")
	public String addRuleView(@RequestParam("keyWords") String keyWords,
			@RequestParam("checkType") String checkType,
			@RequestParam("exceptFields") String exceptFields, ModelMap mod) {
		RuleDO ruleDO = new RuleDO();
		if( checkType.equalsIgnoreCase("0") ){
			ruleDO.setCheckType(1);
		}
		if( checkType.equalsIgnoreCase("1") ){
			ruleDO.setCheckType(2);
		}
		if( checkType.equalsIgnoreCase("2") ){
			ruleDO.setCheckType(4);
		}
		ruleDO.setKeyWords(keyWords);
		ruleDO.setExceptFields(exceptFields);
		SingleResultDO<RuleDO> result = ruleService.createRuleDO(ruleDO);
		RuleDO rule = result.getModel();
		
		mod.addAttribute("keyWords", rule.getKeyWords());
		mod.addAttribute("checkType", rule.getCheckType());
		mod.addAttribute("exceptFields", rule.getExceptFields());
		return "addrule";
	}
	
	/**
	 * �����Աȼ�¼
	 * 
	 * @param mainEnv
	 * @param checkEnv
	 * @param chooseRule
	 * @param mod
	 * @return
	 */
	@RequestMapping( value = "/addcompare.do" )
	public String addCompView(@RequestParam("mainType") String mainEnv,@RequestParam("checkType") String checkEnv, 
			@RequestParam("host") String host, @RequestParam("chooseRule") String[] chooseRule,	ModelMap mod){
		List<ToBeCheckDO> compares = new ArrayList<ToBeCheckDO>();
		VersionDO versionDO = new VersionDO();
		// ת��ѡ��Ĺ���
		String ruleTempStr = "";
		if( chooseRule != null && chooseRule.length > 0 ){
			for( int index = 0; index < chooseRule.length; index ++ ){
				ruleTempStr += chooseRule[index] + ";";
			}
		}else{
			mod.addAttribute( "isSucess", "��Ӷ���ʧ��" );
			return "addcompare";
		}
		
		// �������а汾
		versionDO.setRuleIds(ruleTempStr);
		SingleResultDO<VersionDO> vResult = versionService.createVersionDO(versionDO);
		if( vResult.isSuccess() ){
			Long versionId = vResult.getModel().getId();
			// ��¼�ð汾��host
			if( Integer.parseInt(mainEnv) == 5 || Integer.parseInt( checkEnv ) == 5){
				// ���޸� ������host
				writeStrToHostFile( host, versionId );
			}
			//create compare
			if( isNotEmpty( mainEnv ) && isNotEmpty( checkEnv ) && checkOtherType(checkEnv, host) ){
				List<ItemDO> items = specialItemManager.listForSpecialsWithStyle(15);
				for( ItemDO item : items ){
					ToBeCheckDO beCheckDO = new ToBeCheckDO();
					beCheckDO.setMainEnvrmt( Integer.parseInt( mainEnv ) );
					beCheckDO.setCheckEnvrmt( Integer.parseInt( checkEnv ) );
					beCheckDO.setVersionId( versionId );
					beCheckDO.setisCheck(false);
					beCheckDO.setIsPass(false);
					beCheckDO.setSameReq( item.getId() + "" );
					
					// ����Main and check EvnCOMTID
					beCheckDO.setComtIdMain( createCOMTID() );
					beCheckDO.setComtIdCheck( createCOMTID() );
					
					if( Integer.parseInt(mainEnv) == 5 || Integer.parseInt( checkEnv ) == 5){
						beCheckDO.setFeatures( "host:1" );
					}
					SingleResultDO<ToBeCheckDO> compare = toBeCheckService.createToBeCheckDO(beCheckDO);
					if( compare.isSuccess() ){
						compares.add(compare.getModel());
					}else{
						mod.addAttribute( "isSucess", "��Ӷ���ʧ��" );
						return "addcompare";
					}
				}
				
				mod.addAttribute( "mainEvn", analysisEvn( mainEnv ) );
				mod.addAttribute( "checkEvn", analysisEvn( checkEnv ) );
				mod.addAttribute( "versionId", versionId );
				
				// ���� �������е�excel
				List<String> mainUrlList = new ArrayList<String>();
				List<String> checkUrlList = new ArrayList<String>();
				SingleResultDO<List<ToBeCheckDO>> toBeCheckResult = toBeCheckService.getAllToBeCheckDOByVersionId( versionId );
				if( toBeCheckResult.isSuccess() && toBeCheckResult.getModel() != null && toBeCheckResult.getModel().size() != 0){
					List<ToBeCheckDO> toBeCheckDOs = toBeCheckResult.getModel();
					for( ToBeCheckDO tempDO : toBeCheckDOs ){
						String mainUrl = analysisEvnToUrl( tempDO.getMainEnvrmt() + "" );
						if( mainUrl != null && mainUrl != "" && tempDO.getSameReq() != null && tempDO.getSameReq() != "" && tempDO.getComtIdMain() != 0){
							mainUrl += tempDO.getSameReq();
							SingleResultDO<CommunicationDO> result = comtService.getCommunication( tempDO.getComtIdMain() );
							if( result.isSuccess() ){
								mainUrl += "&trace_id=" + result.getModel().getTraceId();
								mainUrlList.add( mainUrl );
							}
						}
						
						String checkUrl = analysisEvnToUrl( tempDO.getCheckEnvrmt() + "" );
						if( checkUrl != null && checkUrl != "" && tempDO.getSameReq() != null && tempDO.getSameReq() != "" && tempDO.getComtIdCheck() != 0){
							checkUrl += tempDO.getSameReq();
							SingleResultDO<CommunicationDO>  result = comtService.getCommunication( tempDO.getComtIdCheck() );
							if( result.isSuccess() ){
								checkUrl += "&trace_id=" + result.getModel().getTraceId();
								checkUrlList.add( checkUrl );
							}
						}
					}
				} else{
					mod.addAttribute( "isSucess", "��Ӷ��гɹ�������excelʧ��" );
					return "addcompare";
				}
				
				String mainFileName = "result_main_" + versionId + ".xls";
				String checkFileName = "result_check_" + versionId + ".xls";
				boolean mainIsSucess = writeToExcel( mainUrlList, mainFileName );
				boolean checkIsSucess = writeToExcel( checkUrlList, checkFileName );
				
				writeToCSV( mainUrlList, "csv_" + mainFileName );
				writeToCSV( checkUrlList, "csv_" + checkFileName );
				
				if(mainIsSucess && checkIsSucess ){
					mod.addAttribute( "isSucess", "��Ӷ��гɹ�,����excel�ɹ�" );
					mod.addAttribute("mainUrlList", mainUrlList);
					mod.addAttribute("checkUrlList", checkUrlList);
					mod.addAttribute("comtTitle", comtTitle);
				}else{
					mod.addAttribute( "isSucess", "��Ӷ��гɹ�������excelʧ��" );
					return "addcompare";
				}
			}else{
				mod.addAttribute( "isSucess", "��Ӷ���ʧ��" );
				return "addcompare";
			}
		}else{
			mod.addAttribute( "isSucess", "��Ӷ���ʧ��" );
			return "addcompare";
		}
		
		return "addcompare";
	}
	
	/**
	 * �����Աȼ�¼
	 * 
	 * @param mainEnv
	 * @param checkEnv
	 * @param chooseRule
	 * @param mod
	 * @return
	 */
	@RequestMapping( value = "/compareadd.do" )
	public String addCompViewNew(@RequestParam("mainType") String mainEnv,@RequestParam("checkType") String checkEnv, 
			@RequestParam("host") String host, @RequestParam("chooseRule") String[] chooseRule,	ModelMap mod){
		VersionDO versionDO = new VersionDO();
		List<String> mainUrlList = new ArrayList<String>();
		List<String> checkUrlList = new ArrayList<String>();
		int arrNum = 0;
		// ת��ѡ��Ĺ���
		String ruleTempStr = "";
		if( chooseRule != null && chooseRule.length > 0 ){
			for( int index = 0; index < chooseRule.length; index ++ ){
				ruleTempStr += chooseRule[index] + ";";
			}
		}else{
			mod.addAttribute( "isSucess", "��Ӷ���ʧ��" );
			return "compareadd";
		}
		
		// �������а汾
		versionDO.setRuleIds(ruleTempStr);
		SingleResultDO<VersionDO> vResult = versionService.createVersionDO(versionDO);
		if( vResult.isSuccess() ){
			Long versionId = vResult.getModel().getId();
			final int numItem = 100;
			// ��¼�ð汾��host
			writeStrToHostFileNew( analysisEvnToHost( mainEnv, host ), "main", versionId );
			writeStrToHostFileNew( analysisEvnToHost( checkEnv, host ), "check", versionId );
			//create compare
			if( isNotEmpty( mainEnv ) && isNotEmpty( checkEnv ) && checkOtherType(checkEnv, host) ){
				List<ItemDO> items = specialItemManager.listForSpecialsWithEachStyle( numItem );
				for( ItemDO item : items ){
					ToBeCheckDO beCheckDO = new ToBeCheckDO();
					beCheckDO.setMainEnvrmt( Integer.parseInt( mainEnv ) );
					beCheckDO.setCheckEnvrmt( Integer.parseInt( checkEnv ) );
					beCheckDO.setVersionId( versionId );
					beCheckDO.setisCheck(false);
					beCheckDO.setIsPass(false);
					beCheckDO.setSameReq( item.getId() + "" );
					
					// ����Main and check EvnCOMTID
					beCheckDO.setComtIdMain( createCOMTID() );
					beCheckDO.setComtIdCheck( createCOMTID() );
					
					if( Integer.parseInt(mainEnv) == 5 || Integer.parseInt( checkEnv ) == 5){
						beCheckDO.setFeatures( "host:1" );
					}
					SingleResultDO<ToBeCheckDO> compare = toBeCheckService.createToBeCheckDO( beCheckDO );
					
					// ���� �������е�excel
					if( compare.isSuccess() ){
						ToBeCheckDO tempDO = compare.getModel();
						// prepare mainUrl
						String mainUrl = analysisEvnToUrl( tempDO.getMainEnvrmt() + "" );
						if( mainUrl != null && mainUrl != "" && tempDO.getSameReq() != null && tempDO.getSameReq() != "" && tempDO.getComtIdMain() != 0){
							mainUrl += tempDO.getSameReq();
							SingleResultDO<CommunicationDO> result = comtService.getCommunication( tempDO.getComtIdMain() );
							if( result.isSuccess() ){
								mainUrl += "&trace_id=" + result.getModel().getTraceId();
								mainUrlList.add( mainUrl );
							}
						}
						// prepare checkUrl
						String checkUrl = analysisEvnToUrl( tempDO.getCheckEnvrmt() + "" );
						if( checkUrl != null && checkUrl != "" && tempDO.getSameReq() != null && tempDO.getSameReq() != "" && tempDO.getComtIdCheck() != 0){
							checkUrl += tempDO.getSameReq();
							SingleResultDO<CommunicationDO>  result = comtService.getCommunication( tempDO.getComtIdCheck() );
							if( result.isSuccess() ){
								checkUrl += "&trace_id=" + result.getModel().getTraceId();
								checkUrlList.add( checkUrl );
							}
						}
						// ÿ100���½�һ���ļ���������д���ļ�
						if( mainUrlList.size() >= 100 && checkUrlList.size() >= 100 ){
							String mainFileName = "result_main_" + versionId + "_" + arrNum + ".xls";
							String checkFileName = "result_check_" + versionId + "_" + arrNum + ".xls";
							
							boolean mainIsSucess = writeToCSVNew( mainUrlList, "csv_" + mainFileName, versionId );
							boolean checkIsSucess = writeToCSVNew( checkUrlList, "csv_" + checkFileName, versionId );
							
							if(mainIsSucess && checkIsSucess ){
								mod.addAttribute( "isSucess", "��Ӷ��гɹ�,����excel�ɹ�" );
								arrNum ++;
								mainUrlList.clear();
								checkUrlList.clear();
							}else{
								mod.addAttribute( "isSucess", "��Ӷ��гɹ�������excelʧ��" );
								return "compareadd";
							}
						}
					}else{
						mod.addAttribute( "isSucess", "��Ӷ���ʧ��" );
						return "compareadd";
					}
				}
				
				mod.addAttribute( "mainEvn", analysisEvn( mainEnv ) );
				mod.addAttribute( "checkEvn", analysisEvn( checkEnv ) );
				mod.addAttribute( "versionId", versionId );
				mod.addAttribute( "arrNum", arrNum );
			}else{
				mod.addAttribute( "isSucess", "��Ӷ���ʧ��" );
				return "compareadd";
			}
		}else{
			mod.addAttribute( "isSucess", "��Ӷ���ʧ��" );
			return "compareadd";
		}
		
		return "compareadd";
	}
	
	/**
	 * д��汾.xml�ļ�
	 * @param xml
	 * @param versionId
	 */
	public void writeStrToHostFile(String xml, long versionId) {
		try {
			FileOutputStream fos = new FileOutputStream(new File( System.getProperty("user.dir") + "/data/host_version_" + versionId + ".xml"));
			Writer os = new OutputStreamWriter( fos, "UTF-8" );
			os.write( xml );
			os.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * д��汾.xml�ļ����ļ�������ӻ�������
	 * @param xml
	 * @param versionId
	 */
	public void writeStrToHostFileNew(String xml, String evn, long versionId) {
		try {
			String dirName = System.getProperty("user.dir") + "/data/"+ versionId + "/host_version_" + evn + "_" + versionId + ".xml";
			File file = new File( dirName );
			File parent = file.getParentFile();
			if( parent != null && !parent.exists() ){
				parent.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream( file );
			Writer os = new OutputStreamWriter( fos, "UTF-8" );
			os.write( xml );
			os.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkOtherType( String checkEvn, String host ){
		int id = Integer.parseInt( checkEvn );
		if( id == 5 ){
			if( isNotEmpty(host) ){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	@RequestMapping( value = "/showlog.do", method = RequestMethod.GET )
	public String checkResultView(@RequestParam("versionId") String versionId, ModelMap mod){
		
		List<RuleDO> ruleDOList = new ArrayList<RuleDO>();
		List<String> keyWords = new ArrayList<String>();
		String keyWordstr = ";";
		
		SingleResultDO<List<ToBeCheckDO>> toBeCheckResult = toBeCheckService.getAllToBeCheckDOByVersionId( Long.parseLong( versionId ) );
		SingleResultDO<VersionDO> vDO = versionService.getVersionDOById( Long.parseLong( versionId ) );
		if( vDO.isSuccess() ){
			String rulesStr = vDO.getModel().getRuleIds();
			String[] rules = rulesStr.split( ";" );
			if( rules.length > 0 ){
				for( int index = 0; index < rules.length; index ++ ){
					SingleResultDO<RuleDO> result = ruleService.getRuleById( Long.parseLong( rules[index] ) );
					if( result.isSuccess() ){
						ruleDOList.add( result.getModel() );
						String str = result.getModel().getKeyWords();
						keyWords.add( str );
						keyWordstr = keyWordstr + str + ";";
					}
				}
			}
		}
		if( toBeCheckResult.isSuccess() && toBeCheckResult.getModel() != null && toBeCheckResult.getModel().size() != 0){
			List<ToBeCheckDO> toBeCheckDOs = toBeCheckResult.getModel();
			List<LogHtmlDO> logs = new ArrayList<LogHtmlDO>();
			
			String logPaths = "{\"paths\": [" + "\r\n";

			for( int i =0; i < toBeCheckDOs.size(); i ++ ){
				
				ToBeCheckDO tempDO = toBeCheckDOs.get(i);
				//��־
				String logPathName = "{\"path_name\": \""+ versionId + "_";
				String logMethods = "\"methods\": [" + "\r\n";
				String logPath = "{";
				
				// html��־
				String htmlPathName = "";
				
				long mainComtId = tempDO.getComtIdMain();
				long checkComtId = tempDO.getComtIdCheck();
				
				SingleResultDO<List<ConnectionDO>> mainResult = contService.getConnectionByComtId(mainComtId);
				SingleResultDO<List<ConnectionDO>> checkResult = contService.getConnectionByComtId(checkComtId);
				
				if( mainResult.isSuccess() && checkResult.isSuccess() && mainResult.getModel() != null && checkResult.getModel() != null ){
					List<ConnectionDO> mainContResult = mainResult.getModel();
					List<ConnectionDO> checkContResult = checkResult.getModel();
					logPathName += tempDO.getSameReq() + "\",";
					htmlPathName += tempDO.getSameReq();
					for( int index = 0; index < ruleDOList.size(); index++ ){
						RuleDO rule = ruleDOList.get(index);

						switch ( (int) rule.getCheckType() ) {
						case 1:// 1:�����з���
							SingleResultDO<List<ConnectionDO>> result = checkHasRequest( mainContResult, checkContResult, rule.getKeyWords() );
							tempDO.setisCheck(true);
							logMethods += createLogMethod( rule.getKeyWords(), "�Ƿ����", tempDO.getSameReq(), result );
							logs.add( createHtmlLogMethod( htmlPathName, rule.getKeyWords(), "�Ƿ����", tempDO.getSameReq(), result ) );
							break;
						case 2:// 2:У��request��Ϣ
							String eptFields = rule.getExceptFields();
							SingleResultDO<List<ConnectionDO>> reqResult;
							if( isNotEmpty(eptFields) ){
								String[] eptArr = eptFields.split(";");
								reqResult = checkRequest( mainContResult, checkContResult, rule.getKeyWords(), eptArr );
							}else{
								reqResult = checkRequest( mainContResult, checkContResult, rule.getKeyWords() );
							}
							tempDO.setisCheck(true);
							logs.add( createHtmlLogMethod( htmlPathName, rule.getKeyWords(), "request��Ϣ", tempDO.getSameReq(), reqResult ) );
							logMethods += createLogMethod( rule.getKeyWords(), "request��Ϣ", tempDO.getSameReq(), reqResult);
							break;
						case 4:// 3:У��response��Ϣ
							SingleResultDO<List<ConnectionDO>> resResult = checkResponse( getComtTraceId( mainComtId ), getComtTraceId( checkComtId ), mainContResult, checkContResult, rule.getKeyWords() );
							tempDO.setisCheck(true);
							logs.add( createHtmlLogMethod( htmlPathName, rule.getKeyWords(), "response��Ϣ", tempDO.getSameReq(), resResult ) );
							logMethods += createLogMethod( rule.getKeyWords(), "response��Ϣ", tempDO.getSameReq(), resResult);
							break;
						default:
							logMethods += createLogMethod( rule.getKeyWords(), "����û��ƥ�䵽У�����", tempDO.getSameReq(), false);
							break;
						}
						if( index != ruleDOList.size() - 1 ){
							logMethods += ",\r\n";
						}
					}
				}
				
				// ��װ Path
				logPath = logPathName + logMethods + "]}";
				
				if( i == toBeCheckDOs.size() - 1 ){
					logPaths += logPath;
				}else{
					logPaths += logPath + ",\r\n";
				}
			}
			// ��װ Paths
			logPaths += "]}";
			writeStrToFile( logPaths, versionId );
			
			mod.addAttribute( "versionId", versionId );
			mod.addAttribute("logs", logs);
			mod.addAttribute( "logTitle", logTitle );
		}
		return "showlog";
	}
	
	
	@RequestMapping( value = "/showlognew.do", method = RequestMethod.GET )
	public String checkResultViewNew(@RequestParam("versionId") String versionId, @RequestParam("arrnum") String arrNum,
			ModelMap mod){
		
		List<RuleDO> ruleDOList = new ArrayList<RuleDO>();
		List<String> keyWords = new ArrayList<String>();
		String keyWordstr = ";";
		
		SingleResultDO<List<ToBeCheckDO>> toBeCheckResult = toBeCheckService.getAllToBeCheckDOByVersionId( Long.parseLong( versionId ) );
		SingleResultDO<VersionDO> vDO = versionService.getVersionDOById( Long.parseLong( versionId ) );
		if( vDO.isSuccess() ){
			String rulesStr = vDO.getModel().getRuleIds();
			String[] rules = rulesStr.split( ";" );
			if( rules.length > 0 ){
				for( int index = 0; index < rules.length; index ++ ){
					SingleResultDO<RuleDO> result = ruleService.getRuleById( Long.parseLong( rules[index] ) );
					if( result.isSuccess() ){
						ruleDOList.add( result.getModel() );
						String str = result.getModel().getKeyWords();
						keyWords.add( str );
						keyWordstr = keyWordstr + str + ";";
					}
				}
			}
		}
		if( toBeCheckResult.isSuccess() && toBeCheckResult.getModel() != null && toBeCheckResult.getModel().size() != 0){
			List<ToBeCheckDO> toBeCheckDOs = toBeCheckResult.getModel();
			List<LogHtmlDO> logs = new ArrayList<LogHtmlDO>();
			String logPaths = "{\"paths\": [" + "\r\n";
			for( int i =0; i < toBeCheckDOs.size(); i ++ ){
				ToBeCheckDO tempDO = toBeCheckDOs.get(i);
				//��־
				String logPathName = "{\"path_name\": \""+ versionId + "_";
				String logMethods = "\"methods\": [" + "\r\n";
				String logPath = "{";
				
				// html��־
				String htmlPathName = "";
				
				long mainComtId = tempDO.getComtIdMain();
				long checkComtId = tempDO.getComtIdCheck();
				
				SingleResultDO<List<ConnectionDO>> mainResult = contService.getConnectionByComtId(mainComtId);
				SingleResultDO<List<ConnectionDO>> checkResult = contService.getConnectionByComtId(checkComtId);
				
				if( mainResult.isSuccess() && checkResult.isSuccess() && mainResult.getModel() != null && checkResult.getModel() != null ){
					List<ConnectionDO> mainContResult = mainResult.getModel();
					List<ConnectionDO> checkContResult = checkResult.getModel();
					logPathName += tempDO.getSameReq() + "\",";
					htmlPathName += tempDO.getSameReq();
					for( int index = 0; index < ruleDOList.size(); index++ ){
						RuleDO rule = ruleDOList.get(index);

						switch ( (int) rule.getCheckType() ) {
						case 1:// 1:�����з���
							SingleResultDO<List<ConnectionDO>> result = checkHasRequest( mainContResult, checkContResult, rule.getKeyWords() );
							tempDO.setisCheck(true);
							logMethods += createLogMethod( rule.getKeyWords(), "�Ƿ����", tempDO.getSameReq(), result );
							logs.add( createHtmlLogMethod( htmlPathName, rule.getKeyWords(), "�Ƿ����", tempDO.getSameReq(), result ) );
							break;
						case 2:// 2:У��request��Ϣ
							String eptFields = rule.getExceptFields();
							SingleResultDO<List<ConnectionDO>> reqResult;
							if( isNotEmpty(eptFields) ){
								String[] eptArr = eptFields.split(";");
								reqResult = checkRequest( mainContResult, checkContResult, rule.getKeyWords(), eptArr );
							}else{
								reqResult = checkRequest( mainContResult, checkContResult, rule.getKeyWords() );
							}
							tempDO.setisCheck(true);
							logs.add( createHtmlLogMethod( htmlPathName, rule.getKeyWords(), "request��Ϣ", tempDO.getSameReq(), reqResult ) );
							logMethods += createLogMethod( rule.getKeyWords(), "request��Ϣ", tempDO.getSameReq(), reqResult);
							break;
						case 4:// 3:У��response��Ϣ
							SingleResultDO<List<ConnectionDO>> resResult = checkResponse( getComtTraceId( mainComtId ), getComtTraceId( checkComtId ), mainContResult, checkContResult, rule.getKeyWords() );
							tempDO.setisCheck(true);
							logs.add( createHtmlLogMethod( htmlPathName, rule.getKeyWords(), "response��Ϣ", tempDO.getSameReq(), resResult ) );
							logMethods += createLogMethod( rule.getKeyWords(), "response��Ϣ", tempDO.getSameReq(), resResult);
							break;
						default:
							logMethods += createLogMethod( rule.getKeyWords(), "����û��ƥ�䵽У�����", tempDO.getSameReq(), false);
							break;
						}
						if( index != ruleDOList.size() - 1 ){
							logMethods += ",\r\n";
						}
					}
				}
				
				// ��װ Path
				logPath = logPathName + logMethods + "]}";
				
				if( i == toBeCheckDOs.size() - 1 ){
					logPaths += logPath;
				}else{
					logPaths += logPath + ",\r\n";
				}
			}
			// ��װ Paths
			logPaths += "]}";
			writeStrToFile( logPaths, versionId );
			
			mod.addAttribute( "versionId", versionId );
			mod.addAttribute("logs", logs);
			mod.addAttribute( "logTitle", logTitle );
		}
		return "showlognew";
	}
	
	public String getComtTraceId( long comtId ){
		SingleResultDO<CommunicationDO>  comtDO = comtService.getCommunication( comtId );
		if( comtDO.isSuccess() ){
			return comtDO.getModel().getTraceId();
		}else{
			return "";
		}
	}
	
	/**
	 * д��汾.xml�ļ�
	 * @param xml
	 * @param versionId
	 */
	public void writeStrToFile(String xml, String versionId) {
		try {
			FileOutputStream fos = new FileOutputStream(new File( System.getProperty("user.dir") + "/data/version_" + versionId + ".xml"));
			Writer os = new OutputStreamWriter( fos, "UTF-8" );
			os.write( xml );
			os.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ�汾.xml�ļ�
	 * @param xml
	 * @param versionId
	 */
	public String readStrToFile( String versionId ) {
        Reader reader  =   null ;
        String str = "";
		try  {
            reader = new InputStreamReader( new FileInputStream( new File( System.getProperty("user.dir") + "/data/version_" + versionId + ".xml" )) );
             int  tempchar;
             while  ((tempchar  =  reader.read())  !=   - 1 ) {
                 if  ((( char ) tempchar)  !=   '\r' ) {
                	 str += (char)tempchar;
                }
            }
            reader.close();
        }  catch  (Exception e) {
            e.printStackTrace();
        }
        return str;
	}
	
	public String createLogMethod( String keyWords, String ruleType, String sameId, SingleResultDO<List<ConnectionDO>> resultDO ){
		String str = "{";
		String tcId = "\"tc_id\": \"" + keyWords + "_" + ruleType + "_" + sameId + "\",";
		String methodName = "\"method_name\": \"" + keyWords + "����" + ruleType + "У��\",";
		String doc = "\"method_name\": \"" + keyWords + "����" + ruleType + "У��\",";
		String result = "\"result\": \"" + resultDO.isSuccess() + "\",";
		String detail = "\"detail\":\"";
		if( resultDO.isSuccess() ){
			detail += "У��ɹ�";
			if( resultDO.getErrMsg() != null && resultDO.getErrMsg().getErrorCode() !=null && resultDO.getErrMsg().getErrorCode() != "" ){
				detail += "����" + resultDO.getErrMsg() + "\"";
			}else{
				detail += "\"";
			}
		}else{
			detail += resultDO.getErrMsg() + "\"";
		}
		
		str += tcId + methodName + doc + result + detail + "}";
		return str;
	}
	
	public LogHtmlDO createHtmlLogMethod( String pathName, String keyWords, String ruleType, String sameId, SingleResultDO<List<ConnectionDO>> result){
		LogHtmlDO lhDO = new LogHtmlDO();
		
		lhDO.setPathName( pathName );
		lhDO.setTcName( keyWords + "_" + ruleType + "_" + sameId );
		lhDO.setResult( result.isSuccess() );
		if( result.isSuccess() ){
			if( result.getErrMsg() != null && result.getErrMsg().getErrorCode() !=null && result.getErrMsg().getErrorCode() != "" ){
				lhDO.setDetail( result.getErrMsg().getErrorCode() );
			}else{
				lhDO.setDetail( "У��ͨ��" );
			}
		}else{
			lhDO.setDetail( result.getErrMsg().getErrorCode() );
		}
		return lhDO;
	}
	
	public String createLogMethod( String keyWords, String ruleType, String sameId, boolean isSuccess ){
		String str = "{";
		String tcId = "\"tc_id\": \"" + keyWords + "_" + ruleType + "_" + sameId + "\",";
		String methodName = "\"method_name\": \"" + keyWords + "����" + ruleType + "У��\",";
		String doc = "\"method_name\": \"" + keyWords + "����" + ruleType + "У��\",";
		String result = "\"result\": \"" + isSuccess + "\",";
		String detail = "\"detail\":\"";
		if( isSuccess ){
			detail += "У��ɹ�\"";
		}else{
			detail += "У��ʧ��\"";
		}
		
		str += tcId + methodName + doc + result + detail + "}";
		return str;
	}
	
	public SingleResultDO<List<ConnectionDO>> checkHasRequest( List<ConnectionDO> mainContResult, List<ConnectionDO> checkContResult, String keyWords ){
		SingleResultDO<List<ConnectionDO>> result = new SingleResultDO<List<ConnectionDO>>();
		result.setSuccess(true);
		
		List<ConnectionDO> needDOMAC = new ArrayList<ConnectionDO>();
		ConnectionDO main = null;
		ConnectionDO check = null;
		for (int index = 0; index < mainContResult.size(); index++) {
			if (mainContResult.get(index).getUrl().contains(keyWords)) {
				main = mainContResult.get(index);
				break;
			}
		}

		for (int index = 0; index < checkContResult.size(); index++) {
			if (checkContResult.get(index).getUrl().contains(keyWords)) {
				check = checkContResult.get(index);
				break;
			}
		}
		if (main != null && check != null) {
			needDOMAC.add(main);
			needDOMAC.add(check);
			result.setModel(needDOMAC);
		} else {
			if (main == null && check == null){
				result.setSuccess(true);
				result.getErrMsg()
				.putError(
						ErrorCodeConstants.CHECK_AND_MAIN_REQUEST_NOT_CONTAINT_KEYWORDS_ERROR,
						keyWords);
			}else{
				result.setSuccess(false);
				if (main == null)
					result.getErrMsg()
							.putError(
									ErrorCodeConstants.MAIN_REQUEST_NOT_CONTAINT_KEYWORDS_ERROR,
									keyWords);
				if (check == null)
					result.getErrMsg()
							.putError(
									ErrorCodeConstants.CHECK_REQUEST_NOT_CONTAINT_KEYWORDS_ERROR,
									keyWords);
				}
			}
		return result;
	}
	
	public SingleResultDO<List<ConnectionDO>> checkRequest( List<ConnectionDO> mainContResult, List<ConnectionDO> checkContResult, String keyWords, String[] eptArr ){
		SingleResultDO<List<ConnectionDO>> result = new SingleResultDO<List<ConnectionDO>>();
		SingleResultDO<List<ConnectionDO>>hasResult = checkHasRequest( mainContResult, checkContResult, keyWords );
		
		if( hasResult.isSuccess() ){
			List<ConnectionDO> sameReq = hasResult.getModel();
			if( sameReq == null || sameReq.isEmpty() ){
				result.setErrMsg( hasResult.getErrMsg() );
				result.setSuccess(true);
			}else{
				ConnectionDO mainCont = sameReq.get(0);
				ConnectionDO checkCont = sameReq.get(1);
				
				// check requset header
//				Map<String, List<String>>mainHeader = mainCont.getReqDO().getHeader();
//				Map<String, List<String>>checkHeader = checkCont.getReqDO().getHeader();
				
				// check Param
				String mainUrl = mainCont.getUrl();
				String checkUrl = checkCont.getUrl();
				Map< String, String > mainP;
				Map< String, String > checkP;
				if( eptArr != null ){
					mainP = getParamFromUrlWihtEpt( mainUrl, eptArr );
					checkP = getParamFromUrlWihtEpt( checkUrl, eptArr );
				}else{
					mainP = getParamFromUrl(mainUrl);
					checkP = getParamFromUrl(checkUrl);
				}
				
				SingleResultDO<List<String>> mapResult = checkMap( mainP, checkP );
				if( mapResult.isSuccess() ){
					result.setSuccess(true);
					result.setModel(mainContResult);
				}else{
					result.setSuccess(false);
					result.setErrMsg( mapResult.getErrMsg() );
				}
			}
			
		}else{
			result.setSuccess(false);
			result.setErrMsg( hasResult.getErrMsg() );
		}
		
		return result;
	}
	
	public SingleResultDO<List<ConnectionDO>> checkRequest( List<ConnectionDO> mainContResult, List<ConnectionDO> checkContResult, String keyWords ){
		return checkRequest( mainContResult, checkContResult, keyWords, null );
	}
	
	public SingleResultDO<List<String>> checkMap( Map<String,String> main, Map<String,String> check ){
		SingleResultDO<List<String>> result = new SingleResultDO<List<String>>();
		List<String> keyList = new ArrayList<String>();
		result.setSuccess(true);
		Iterator<String> mainIt = main.keySet().iterator();
		
		while( mainIt.hasNext() ){
			String mainKey = (String) mainIt.next();
			
			if( check.containsKey(mainKey) ){
				if( main.get(mainKey).equalsIgnoreCase( check.get(mainKey) ) ){
					result.setSuccess(true);
					keyList.add( mainKey );
				}else{
					result.setSuccess(false);
					result.getErrMsg().put( ErrorCodeConstants.CHECK_PARAM_VALUE_NOE_EQUAL, mainKey );
					break;
				}
			}else{
				result.setSuccess(false);
				result.getErrMsg().put( ErrorCodeConstants.CHECK_NOT_CONTAIN_PARAM, mainKey );
				break;
			}
		}
		return result;
	}
	
	public Map<String,String> getParamFromUrl( String url ){
		Map<String, String> params = new HashMap<String, String>();
		String[] urlAarray = url.split("[?]");
		if( urlAarray.length == 2 ){
			String[] pArray = urlAarray[1].split("&");
			if( pArray.length>0 ){
				for( int index = 0; pArray.length > index; index ++ ){
					String[] temp = pArray[index].split("=");
					if( temp.length == 2 ){
						params.put(temp[0], temp[1]);
					}
					if( temp.length == 1 ){
						params.put(temp[0], "");
					}
				}
			}
		}
		return params;
	}
	
	public Map<String,String> getParamFromUrlWihtEpt( String url, String[] eptArr ){
		Map<String, String> params = new HashMap<String, String>();
		String[] urlAarray = url.split("[?]");
		if( urlAarray.length == 2 ){
			String[] pArray = urlAarray[1].split("&");
			if( pArray.length>0 ){
				for( int index = 0; pArray.length > index; index ++ ){
					String[] temp = pArray[index].split("=");
					if( temp.length == 2 ){
						if( isNotExpectStr( temp[0], eptArr ) )
							params.put(temp[0], temp[1]);
					}
					if( temp.length == 1 ){
						if( isNotExpectStr( temp[0], eptArr ) )
							params.put(temp[0], "");
					}
				}
			}
		}
		return params;
	}
	
	public boolean isNotExpectStr( String str, String[] eptArr ){
		boolean isExpect = true;
		for( int i = 0; i < eptArr.length; i++ ){
			if( str.equalsIgnoreCase( eptArr[i] ) ){
				isExpect = false;
				break;
			}
		}
		return isExpect;
	}
	
	
	public SingleResultDO<List<ConnectionDO>> checkResponse( String mainComtId, String checkComtId, List<ConnectionDO> mainContResult, List<ConnectionDO> checkContResult, String keyWords ){
		SingleResultDO<List<ConnectionDO>> result = new SingleResultDO<List<ConnectionDO>>();
		SingleResultDO<List<ConnectionDO>>hasResult = checkHasRequest( mainContResult, checkContResult, keyWords );
		
		if( hasResult.isSuccess() ){
			List<ConnectionDO> sameReq = hasResult.getModel();
			if( sameReq == null || sameReq.isEmpty() ){
				result.setErrMsg( hasResult.getErrMsg() );
				result.setSuccess(true);
			}else{
				ConnectionDO mainCont = sameReq.get(0);
				ConnectionDO checkCont = sameReq.get(1);
				
				// check response header
//				Map<String, List<String>>mainHeader = mainCont.getResDO().getHeader();
//				Map<String, List<String>>checkHeader = checkCont.getResDO().getHeader();
				
				// check response content
				String mainContent = removeDaimon( removeStyle( new String( mainCont.getResDO().getContent() ) ).replaceAll(  "" + mainComtId, "" ) );
				String checkContent = removeDaimon( removeStyle( new String( checkCont.getResDO().getContent() ) ).replaceAll( "" + checkComtId, "") );
				
				
				
				boolean contEqual = Arrays.equals( mainContent.getBytes(), checkContent.getBytes() );
				if( contEqual ){
					result.setSuccess(true);
					result.setModel(mainContResult);
				}else{
					result.setSuccess(false);
					result.getErrMsg().put( ErrorCodeConstants.CHECK_RESPONSE_CONTENT_NOT_SAME,  keyWords );
				}
			}
		}else{
			result.setSuccess(false);
			result.setErrMsg( hasResult.getErrMsg() );
		}
		
		return result;
	}
	
	public String removeDaimon( String str ){
		str = str.replaceAll( "itempre.taobao.com", "item.taobao.com");
		str = str.replaceAll( "itembeta1.taobao.com", "item.taobao.com");
		str = str.replaceAll( "itembeta2.taobao.com", "item.taobao.com");
		str = str.replaceAll( "itempre.beta.taobao.com", "item.taobao.com");
		str = str.replaceAll( "item.pre.taobao.com", "item.taobao.com");
		
		str = str.replaceAll("limitTime:[^,]*,", "");
		str = str.replaceAll(",\"tkn\":[^,]*}", "}}");
		str = str.replaceAll( "jsonp[^,]*\\(", "(");
		return str;
	}
	
	@RequestMapping( value = "/compareing.do", method = RequestMethod.GET)
	public String startCompare( @RequestParam("versionId") String versionId, @RequestParam("num") String numStr,ModelMap mod ){
		int arrNum = Integer.parseInt( numStr );
		//����host
		String dirStr = System.getProperty("user.dir") + "/data/"+ versionId;
		String mainHostStr = getHostStr( dirStr + "/host_version_main_" + versionId + ".xml" );
		if( mainHostStr != null && !mainHostStr.isEmpty() ){
			if( !updateHostFile( mainHostStr ) ){
				mod.addAttribute( "isSuccess", "�޸�host�ļ�ʧ��");
				return "compareing";
			}
		}
		List<String> mainItems = null;
		for( int index = 0; index < arrNum; index++ ){
			String mainItemsDir = dirStr + "/csv_result_main_" + versionId + "_" + index + ".xls";
			try {
				BufferedReader reader = new BufferedReader(new FileReader( mainItemsDir ));
				mainItems = getItemUrlString( reader );
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// ���� chrome ��·��
			System.setProperty( "webdriver.chrome.driver", "D:\\Selenium WebDriver\\chromedriver_win32\\chromedriver.exe");
			// ����һ�� ChromeDriver �Ľӿڣ��������� Chrome
			// ����һ�� Chrome �������ʵ��
			WebDriver mainDriver = new ChromeDriver();

			for( int i = 0; i < mainItems.size(); i ++ ){
				mainDriver.get( mainItems.get(i) );
				try {
					Thread.sleep( 2000 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			mainDriver.quit();
			mainItems.clear();
			
			// ��У��ı���
			String checkHostStr = getHostStr( dirStr + "/host_version_check_" + versionId + ".xml" );
			if( checkHostStr != null && !checkHostStr.isEmpty() ){
				if( !updateHostFile( checkHostStr ) ){
					mod.addAttribute( "isSuccess", "�޸�host�ļ�ʧ��");
					return "compareing";
				}
			}
			List<String> checkItems = null;
			String checkItemsDir = dirStr + "/csv_result_check_" + versionId + "_" + index + ".xls";
			try {
				BufferedReader reader = new BufferedReader(new FileReader( checkItemsDir ));
				checkItems = getItemUrlString( reader );
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// ������������򿪱���
			System.setProperty( "webdriver.chrome.driver", "D:\\Selenium WebDriver\\chromedriver_win32\\chromedriver.exe");
			WebDriver checkDriver = new ChromeDriver();

			for( int i = 0; i < checkItems.size(); i ++ ){
				checkDriver.get( checkItems.get(i) );
				try {
					Thread.sleep( 2000 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			checkDriver.quit();
			checkItems.clear();
		}
		mod.addAttribute( "isSuccess", "���гɹ�" );
		return "compareing";
	}
	
	private  List<String> getItemUrlString( BufferedReader reader) throws IOException {
		String line = reader.readLine();
		List<String> items = new ArrayList<String>();
		while (ReadLineIsNotEmpty(line)) {
			String row[] = line.split(",");// CSV��ʽ�ļ�Ϊ���ŷָ����ļ���������ݶ����з�
			items.add( row[1].replaceAll("\"", "") );
			line = reader.readLine();
		}
		return items;
	}
	
	private boolean updateHostFile( String hostStr ){
		try { 
			
			String hostSpt = ";";
			String ipSpt = " ";
			
			List<String> ips = new ArrayList<String>();
			List<String> domains = new ArrayList<String>();
			
	        String[] ipAndDomains = hostStr.split( hostSpt );
	            
	        if( ipAndDomains != null && ipAndDomains.length > 0 ){
	          	for( int index = 0; index < ipAndDomains.length; index ++ ){
	          		String[] temp = ipAndDomains[index].split( ipSpt );
	           		if( temp != null && temp.length > 1 ){
	          			ips.add( temp[0] );
	          			String domain = "";
	          			for( int i = 1; i < temp.length; i ++ ){
	          				domain += temp[i] + " ";
	          			}
	          			domains.add( domain.trim() );
	           		}else{
	           			System.out.println( "ERROR: get ip and domain error. " + ipAndDomains[index] );
	           			return false;
	           		}
	           	}
	           }else{
	           	System.out.println( "ERROR: get ip and domain error. " + hostStr );
	           	return false;
	        }
	        
	        updateHosts( ips, domains );
			// ��hosts�л�ʵʱ��Ч
			Security.setProperty("networkaddress.cache.ttl", "0");
			Security.setProperty("networkaddress.cache. negative .ttl", "0");
			Runtime.getRuntime().exec("ipconfig  /flushdns");
			
			return true;
		} catch (IOException e) { 
		     System.out.println("ERROR: call runtime to flushdns exception: " + e.getMessage());     
		     return false;
		}
	}
	
	/**
	 * ����host
	 * 1���������ͬ�İ󶨣�������host
	 * 2������в��ְ���ͬ��ע�͵�ԭ��host�󶨣�����µİ�
	 * 3�����û�иð󶨣�����°�
	 * @param ips
	 * @param domains
	 * @return
	 */
	private SameHost updateHosts( List<String>ips, List<String>domains ){
		if( ips.size() != domains.size() ){
			return SameHost.error;
		}
		
		/** 
         * ��ȡhost�ļ� 
         */ 
        String fileName = getHostFile(); 
        List<?> hostFileDataLines = null; 
        try { 
            hostFileDataLines = FileUtils.readLines(new File(fileName)); 
        } catch (IOException e) { 
            System.out.println("Reading host file occurs error: " + e.getMessage()); 
            return SameHost.error;
        }
        
        List<String> newLinesList = new ArrayList<String>();
        String split = " ";
        /**
         * ָ��domain�Ƿ���ڣ�������ڣ���׷��
         * ��ʶ�����ļ��Ƿ��и��£����磺���ָ����IP�������Ѿ���host�ļ��д��ڣ�������д�ļ� 
         * Ĭ�����а���Ҫ��host�ļ�������
         */
        List<Boolean> needAdd = new ArrayList<Boolean>();
        for( int index = 0; index < ips.size(); index ++ ){
        	needAdd.add( true );
        }
        
        for( Object line : hostFileDataLines ){
        	String strLine = line.toString();
        	String temp = removeStyle( strLine );
        	if( !strLine.startsWith( "#" ) && temp != null && !temp.isEmpty()  ){
        		String[] host = strLine.split( split );
        		if( host.length < 2 ){
        			System.out.println("host file is error. start:" + strLine + "end");
        			return SameHost.error;
        		}
        		
        		// ԭ��������ת����list
    			List<String> orgDomains = new ArrayList<String>(); 
    			for( int j = 1; j < host.length; j ++ ){
					orgDomains.add( host[j].trim() );
				}
        		SameHost lineSame = null;
        		// ��Ҫ���µİ�ѭ��
        		for( int i = 0; i < ips.size(); i ++ ){
        			// �°󶨵�����stringת��list
        			List<String> updateDomains = new ArrayList<String>();
        			String[] domainStrs = domains.get(i).split(split);
        			if( domainStrs.length <= 0 ){
        				return SameHost.error;
        			}
        			for( int j = 0; j < domainStrs.length; j ++ ){
        				updateDomains.add( domainStrs[j].trim() );
        			}
        			
        			// IP��ͬ
        			if( host[0].trim().equalsIgnoreCase( ips.get( i ).trim() ) ){
        				lineSame = judgeSameDomain( orgDomains, updateDomains );
        				// IP��������ȫ��ͬ����������󶨲���Ҫ���
        				if( lineSame == SameHost.same ){
    						needAdd.set( i, false );
    						break;
        				}
        				// ����������ͬ
        				if( lineSame == SameHost.part ){
    						break;
        				}
        				
        			}else{// IP��ͬ
        				lineSame = judgeSameDomain( orgDomains, updateDomains );
        				// ������ͬ��IP��ͬ����Ҫע�͵�
        				if( lineSame == SameHost.same ){
        					lineSame = SameHost.part;
        					break;
        				}
        				// ����������ͬ
        				if( lineSame == SameHost.part ){
    						break;
        				}
        			}
        		}
        		// ������IP��ͬ��IP��ͬ������ͬ������Ҫע�ͣ���д��host
				if( lineSame.equals( SameHost.different ) )
					newLinesList.add( strLine );
				// ����������ͬ����Ҫע��
				if( lineSame.equals( SameHost.part ) )
					newLinesList.add( "#" + strLine );
				// ��ͬ�İ󶨣�����Ҫ�ٶ������
				if( lineSame.equals( SameHost.same ) )
					newLinesList.add( strLine );
        	}else{
        		newLinesList.add( strLine );
        	}
        }
        
        int temp = 0;
        for( int index = 0; index < needAdd.size(); index ++ ){
        	if( needAdd.get( index ) ){
        		if( temp == 0 )
        			newLinesList.add( "#luanjia������" );
        		newLinesList.add( ips.get(index) + split + domains.get(index) );
        		temp ++;
        	}
        }
        /** 
         * д�趨�ļ�
         */
		try {
			FileUtils.writeLines( new File(fileName), newLinesList );
			if( temp == 0 )
				return SameHost.different;
			if( temp < ips.size() )
				return SameHost.part;
			if( temp == ips.size() )
				return SameHost.same;
			return SameHost.error;
		} catch (IOException e) {
			System.out.println("Updating host file occurs error: " + e.getMessage());
			return SameHost.error;
		}
	}
	
	/**
	 * ȥ�� removeString �ַ����еĿո񡢻س�
	 * @param removeString
	 * @return
	 * @author luanjia
	 */
	protected static String removeStyle( String removeString ) {
		String subString = "";
		if( removeString != "" && removeString != null ){
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			java.util.regex.Matcher actrualJsonStrMa = p.matcher( removeString );
			subString = actrualJsonStrMa.replaceAll( "" );	
		}
		return subString;
	}
	/**
	 * �ж����Ƿ���ͬ
	 * @param originalDomains
	 * @param newDomains
	 * @return
	 */
	private SameHost judgeSameDomain( List<String> originalDomains, List<String> newDomains ){
		boolean codeSame = false;
 		int domainSameNum = 0;
		if( originalDomains.size() == newDomains.size() ){
			for( int i = 0; i < originalDomains.size(); i++ ){
				codeSame = false;
				for( int j = 0; j < newDomains.size(); j++ ){
					if( originalDomains.get( i ).equalsIgnoreCase( newDomains.get( j ) ) ){
						codeSame = true;
						break;
					}
				}
				if( codeSame == true ){
					domainSameNum ++;
				}
			}
		}else{
			for( int i = 0; i < originalDomains.size(); i++ ){
				codeSame = false;
				for( int j = 0; j < newDomains.size(); j++ ){
					if( originalDomains.get( i ).equalsIgnoreCase( newDomains.get( j ) ) ){
						codeSame = true;
						break;
					}
				}
				if( codeSame == true ){
					domainSameNum ++;
				}
			}
		}
		
		return isSameDomain( domainSameNum, newDomains.size() );
	}
	
	private SameHost isSameDomain( int sameNum, int domainSize ){
		if( sameNum == 0 )
			return SameHost.different;
		if( sameNum == domainSize )
			return SameHost.same;
		if( sameNum < domainSize )
			return SameHost.part;
		return SameHost.different;
	}
	/**
	 * ��ͬ�ĳ̶ȣ�
	 * different ��ʾ��ȫ��ͬ
	 * same ��ʾ��ȫ��ͬ
	 * part ��ʾ������ͬ
	 * @author luanjia
	 *
	 */
	public enum SameHost {
		different, same, part, error;
	}
	
	/** 
     * ��ȡhost�ļ�·�� 
     * @return 
     */ 
	private String getHostFile() { 
        String fileName = null; 
        // �ж�ϵͳ 
        if ("linux".equalsIgnoreCase(System.getProperty("os.name"))) { 
            fileName = "/etc/hosts"; 
        } else { 
            fileName = System.getenv("windir") + "\\system32\\drivers\\etc\\hosts"; 
        } 
        return fileName; 
    } 
	
	private String getHostStr( String hostDirStr ){
		String host = null;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader( hostDirStr ));
			host = getNeedDaString( reader );
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return host;
	}
	
	public String getNeedDaString( BufferedReader reader) throws IOException {
		String line = reader.readLine();
		String host = null;
		while ( ReadLineIsNotEmpty(line) ) {
			host += line;
			line = reader.readLine();
		}
		return host;
	}
	
	private boolean ReadLineIsNotEmpty( String readLine ){
		if( null != readLine && !readLine.isEmpty() ){
			return true;
		}else {
			return false;
		}
	}
	
	@RequestMapping( value = "/createfile.do" )
	public String createExcelView(@RequestParam("versionId") String versionId, ModelMap mod){
		
		List<String> mainUrlList = new ArrayList<String>();
		List<String> checkUrlList = new ArrayList<String>();
		SingleResultDO<List<ToBeCheckDO>> toBeCheckResult = toBeCheckService.getAllToBeCheckDOByVersionId( Long.parseLong( versionId ) );
		if( toBeCheckResult.isSuccess() && toBeCheckResult.getModel() != null && toBeCheckResult.getModel().size() != 0){
			List<ToBeCheckDO> toBeCheckDOs = toBeCheckResult.getModel();
			for( ToBeCheckDO tempDO : toBeCheckDOs ){
				String mainUrl = analysisEvnToUrl( tempDO.getMainEnvrmt() + "" );
				if( mainUrl != null && mainUrl != "" && tempDO.getSameReq() != null && tempDO.getSameReq() != ""){
					mainUrl += tempDO.getSameReq();
					mainUrlList.add( mainUrl );
				}
				
				String checkUrl = analysisEvnToUrl( tempDO.getCheckEnvrmt() + "" );
				if( checkUrl != null && checkUrl != "" && tempDO.getSameReq() != null && tempDO.getSameReq() != ""){
					checkUrl += tempDO.getSameReq();
					checkUrlList.add( checkUrl );
				}
			}
		}
		
		String mainFileName = "result_main_" + versionId + ".xls";
		String checkFileName = "result_check_" + versionId + ".xls";
		writeToExcel( mainUrlList, mainFileName );
		writeToExcel( checkUrlList, checkFileName );
		
		writeToCSV( mainUrlList, "csv_" + mainFileName );
		writeToCSV( checkUrlList,"csv_" + checkFileName );
		return "createfile";
	}
	
	private boolean writeToCSV( List<String>urlList, String fileName ){
		boolean isSucess = true;
		String xml = "";
		for( int index = 0; index < urlList.size(); index ++ ){
			xml += "\"" + index + "\"," + "\"" + urlList.get(index) + "\"\r\n";
		}
		try{
			FileOutputStream fos = new FileOutputStream(new File( System.getProperty("user.dir") + "/data/" + fileName ));
			Writer os = new OutputStreamWriter( fos, "UTF-8" );
			os.write( xml );
			os.flush();
			fos.close();
		} catch (IOException e) {
			isSucess =  false;
			System.out.println("�������󣬴���ѶϢ��" + e.toString());
		}
		return isSucess;
	}
	
	private boolean writeToCSVNew( List<String>urlList, String fileName, long versionId ){
		boolean isSucess = true;
		String xml = "";
		for( int index = 0; index < urlList.size(); index ++ ){
			xml += "\"" + index + "\"," + "\"" + urlList.get(index) + "\"\r\n";
		}
		try{
			FileOutputStream fos = new FileOutputStream(new File( System.getProperty("user.dir") + "/data/" + versionId + "/" + fileName ));
			Writer os = new OutputStreamWriter( fos, "UTF-8" );
			os.write( xml );
			os.flush();
			fos.close();
		} catch (IOException e) {
			isSucess =  false;
			System.out.println("�������󣬴���ѶϢ��" + e.toString());
		}
		return isSucess;
	}
	
	private boolean writeToExcel( List<String> urlList, String fileName ){
		boolean isSucess = true;
		try {
			FileOutputStream fos = new FileOutputStream( System.getProperty("user.dir") + "/data/" + fileName);
			HSSFWorkbook wb = new HSSFWorkbook();// ����������
			HSSFSheet sheet = wb.createSheet();// ����������
			wb.setSheetName(0, "process");// ���ù�������
			HSSFRow row = null;
			// ����һ��
			HSSFCell cell = null;
			// ����һ�������
			row = sheet.createRow(0);
			// ����һ���µ��У�ע���ǵ�����(�м�������Ǵ�0����)
			cell = row.createCell(0);
			// �趨����������ִ�Ҫ����˫λԪ
			cell.setCellValue("����");
			cell = row.createCell(1);
			cell.setCellValue("������������");
			cell = row.createCell(2);
			cell.setCellValue("�Ƿ�ִ��");
			cell = row.createCell(3);
			cell.setCellValue("ִ�д���");
			cell = row.createCell(4);
			cell.setCellValue("��ע");
			cell = row.createCell(5);
			cell.setCellValue("itemurl");
			for( int index = 1; index < urlList.size() + 1; index ++ ){
				row = sheet.createRow( index );
				cell = row.createCell( 0 );
				cell.setCellValue( index );
				cell = row.createCell( 1 );
				cell.setCellValue( "httpfilter" );
				cell = row.createCell( 2 );
				cell.setCellValue( "Y" );
				cell = row.createCell( 3 );
				cell.setCellValue( 1 );
				cell = row.createCell( 5 );
				cell.setCellValue( urlList.get(index -1) );
			}
			wb.createSheet();// ����������
			wb.setSheetName(1, "initsql");// ���ù�������
			wb.createSheet();// ����������
			wb.setSheetName(2, "RebackSql");// ���ù�������
			wb.createSheet();// ����������
			wb.setSheetName(3, "DeleteSql");// ���ù�������
			
			wb.write(fos);
			fos.close();
		} catch (IOException e) {
			isSucess =  false;
			System.out.println("�������󣬴���ѶϢ��" + e.toString());
		}
		return isSucess;
	}
	
	public void appendMethodB(String fileName, String content) {
		try {
			// ��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public long createCOMTID() {
		UUID uuid = UUID.randomUUID();
		long comtId = -1;
		SingleResultDO<List<CommunicationDO>> comtDOs = comtService.fetchComtByTraceId(uuid.toString());
		if( comtDOs.isSuccess() && CollectionUtils.isNotEmpty( comtDOs.getModel() ) ){
			comtId = comtDOs.getModel().get(0).getId();
		}
		return comtId;
	}
	@RequestMapping(value = "/runrule.do")
	public String runRuleView(ModelMap mod ){
		List<RuleDO> rules = new ArrayList<RuleDO>();
		//��ȡ���еĹ���
		SingleResultDO<List<RuleDO>> result = ruleService.searchAllRules();
		if( result.isSuccess() ){
			rules = result.getModel();
		}
		List<ItemDO> items = specialItemManager.listForSpecialsWithStyle(15);
		mod.addAttribute("rules", rules);
		mod.addAttribute("title", ruleTitle);
		mod.addAttribute("itemTitle", itemTitle);
		mod.addAttribute("items", items);
		
		return "runrule";
	}
	
	public String analysisEvn( String env ){
		//�ж�������
		if( env.equalsIgnoreCase("0") ){
			return "����";
		}
		if( env.equalsIgnoreCase("1") ){
			return "beta";
		}
		if( env.equalsIgnoreCase("2") ){
			return "Ԥ��";
		}
		if( env.equalsIgnoreCase("3") ){
			return "�Ҷ�Ԥ��";
		}
		if( env.equalsIgnoreCase("4") ){
			return "�Ҷ�beta";
		}
		return null;
	}
	
	public String analysisEvnToHost( String env, String host ){
		//�ж�������
		if( env.equalsIgnoreCase("0") ){
			return "";
		}
		if( env.equalsIgnoreCase("1") ){
			return detailskipBeta;
		}
		if( env.equalsIgnoreCase("2") ){
			return detailskipBeta;
		}
		if( env.equalsIgnoreCase("3") ){
			return detailskipPre;
		}
		if( env.equalsIgnoreCase("4") ){
			return detailskipPre;
		}
		if( env.equalsIgnoreCase("5") ){
			return host;
		}
		return null;
	}
	
	public String analysisEvnToUrl( String env ){
		//�ж�������
		if( env.equalsIgnoreCase("0") ){
			return "http://item.taobao.com/item.htm?id=";
		}
		if( env.equalsIgnoreCase("1") ){
			return "http://itembeta1.taobao.com/item.htm?id=";
		}
		if( env.equalsIgnoreCase("2") ){
			return "http://itempre.taobao.com/item.htm?id=";
		}
		if( env.equalsIgnoreCase("3") ){
			return "http://itempre.beta.taobao.com/item.htm?id=";
		}
		if( env.equalsIgnoreCase("4") ){
			return "http://item.beta.taobao.com/item.htm?id=";
		}
		if( env.equalsIgnoreCase("5") ){
			return "http://item.taobao.com/item.htm?id=";
		}
		return null;
	}
	
	public boolean isNotEmpty( String str ){
		if( str != null && !str.isEmpty() )
			return true;
		return false;
	}
}
