package com.internousdev.i1810a.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.i1810a.dao.CartInfoDAO;
import com.internousdev.i1810a.dao.DestinationInfoDAO;
import com.internousdev.i1810a.dao.UserInfoDAO;
import com.internousdev.i1810a.dto.CartInfoDTO;
import com.internousdev.i1810a.dto.DestinationInfoDTO;
import com.internousdev.i1810a.dto.PurchaseHistoryInfoDTO;
import com.internousdev.i1810a.dto.UserInfoDTO;
import com.internousdev.i1810a.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware {
	private String loginUserId;
	private String loginPassword;
	private boolean status;
	private Map<String, Object> session;
	private boolean loginFlg;
	public String execute(){
		String result = ERROR;
		//複数タブ操作時、片方の画面でログイン画面に残っていた場合
		if(String.valueOf(session.get("logined")).equals("1")){
			return "injustice";//エラーページへ
		}
		//リンク直打ち対策
		if(loginUserId == null || loginPassword == null){
			return "injustice";//エラーページへ
		}
		//リスト宣言
		List<String> loginErrorMessageList = new ArrayList<String>();
		List<String> loginIdMessage = new ArrayList<String>();
		List<String> loginPassMessage = new ArrayList<String>();
		//エラーメッセージをリセット
		session.remove("loginIdMessage");
		session.remove("loginPassMessage");
		session.remove("loginErrorMessageList");
		//チェックが入っていた場合、ログインに失敗してもIdを保存
		session.put("loginUserId", loginUserId);
		session.put("status", status);
		//正規表現チェック
		InputChecker check = new InputChecker();
		loginIdMessage = check.checkPattern("ログインID", loginUserId, 1, 8, "半角英数字");
		loginPassMessage = check.checkPattern("パスワード", loginPassword, 1, 16, "半角英数字");
		if(!(loginIdMessage.size() == 0 && loginPassMessage.size() == 0)){
			session.put("loginIdMessage", loginIdMessage);
			session.put("loginPassMessage", loginPassMessage);
		}else{
				boolean loginCheck = UserInfoDAO.doLoginCheck(loginUserId, loginPassword);
				if(!(loginCheck)){
					loginErrorMessageList.add("ユーザー名　または　パスワードが間違っています。");
				}

				if(loginErrorMessageList.size() == 0){
					result = SUCCESS;
					//ログイン状態にする
					session.put("logined", "1");
					//ユーザー情報をsessionに格納
					UserInfoDTO dto = UserInfoDAO.getUserDetails(loginUserId);
					session.put("familyName",dto.getFamilyName());
					session.put("firstName", dto.getFirstName());
					session.put("familyNameKana", dto.getFamilyNameKana());
					session.put("firstNameKana", dto.getFirstNameKana());
					//仕様書でふりがなを結合させるという指示があったため、結合しています
					String hurigana = dto.getFamilyNameKana().concat(" "+dto.getFirstNameKana());
					session.put("hurigana", hurigana);
					session.put("sex", dto.getSex());
					//データベースにはint型で入れる必要があるため、ここで変換しています
					if(dto.getSex() == 0){
						session.put("sexNum", 0);
						session.put("sexValue", "男性");
					}else{
						session.put("sexNum", 1);
						session.put("sexValue", "女性");
					}
					session.put("email", dto.getEmail());
					session.put("userId", dto.getUserId());
					//loginFlgを取得(nullの場合はfalse)
					String loginFlgStr = String.valueOf(session.get("loginFlg"));
					loginFlg = Boolean.valueOf(loginFlgStr);
					//カート内に商品が入っていた場合、決済画面に移動させるため、
					//loginFlgというフラッグをもらって判定しています(SettlementConfirmActionで発行)
					if(loginFlg){
						DestinationInfoDAO destinationInfoDAO = new DestinationInfoDAO();
						List<DestinationInfoDTO> destinationInfoDtoList = new ArrayList<>();
						try {
							if (session.containsKey("tempUserId")) {
								 CartInfoDAO cartInfoDao = new CartInfoDAO();
								 //未ログイン者に割り当てられるID(tempUserId)をloginUserIdに置き換えています
								 cartInfoDao.linkToLoginId(session.get("tempUserId").toString(), loginUserId);
								List<CartInfoDTO> cartInfoDtoList = cartInfoDao.getCartInfoDtoList(loginUserId);
								session.put("cartInfoDtoList", cartInfoDtoList);

								List<PurchaseHistoryInfoDTO> purchaseHistoryInfoDtoList = new ArrayList<PurchaseHistoryInfoDTO>();

								for(CartInfoDTO DTO:cartInfoDtoList){
									PurchaseHistoryInfoDTO purchaseHistoryInfoDTO = new PurchaseHistoryInfoDTO();
									purchaseHistoryInfoDTO.setUserId(loginUserId);
									purchaseHistoryInfoDTO.setProductId(DTO.getProductId());
									purchaseHistoryInfoDTO.setProductName(DTO.getProductName());
									purchaseHistoryInfoDTO.setProductNameKana(DTO.getProductNameKana());
									purchaseHistoryInfoDTO.setImageFilePath(DTO.getImageFilePath());
									purchaseHistoryInfoDTO.setImageFileName(DTO.getImageFileName());
									purchaseHistoryInfoDTO.setPrice(DTO.getPrice());
									purchaseHistoryInfoDTO.setReleaseCompany(DTO.getReleaseCompany());
									purchaseHistoryInfoDTO.setProductCount(DTO.getProductCount());
									purchaseHistoryInfoDTO.setSubtotal(DTO.getSubtotal());
									purchaseHistoryInfoDtoList.add(purchaseHistoryInfoDTO);
								}
									session.put("purchaseHistoryInfoDtoList", purchaseHistoryInfoDtoList);
								}
								destinationInfoDtoList = destinationInfoDAO.getDestinationInfo(loginUserId);
								Iterator<DestinationInfoDTO> iterator = destinationInfoDtoList.iterator();
								if(!(iterator.hasNext())) {
									destinationInfoDtoList = null;
								}
								session.put("destinationInfoDtoList", destinationInfoDtoList);
							} catch (SQLException e) {
								e.printStackTrace();
							}
							result ="settlement";

					}else{

						result =  SUCCESS;
					}

				}else{
					session.put("logined",0);
					session.put("loginErrorMessageList",loginErrorMessageList);
					result =  ERROR;
				}
		}

		return result;
	}
	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getLoginUserId() {
		return loginUserId;
	}
	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public boolean isLoginFlg() {
		return loginFlg;
	}
	public void setLoginFlg(boolean loginFlg) {
		this.loginFlg = loginFlg;
	}
}
