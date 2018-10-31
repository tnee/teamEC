package com.internousdev.i1810a.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.i1810a.dao.DestinationInfoDAO;
import com.internousdev.i1810a.dto.CartInfoDTO;
import com.internousdev.i1810a.dto.DestinationInfoDTO;
import com.internousdev.i1810a.dto.PurchaseHistoryInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

//Cart.jspですでにエラー表示がされた後にチェックを入れて遷移してきた場合にセッションをクリアする処理を以下に実装するが、
//ブラウザの「戻る」ボタンは特別なことをしない限り、通常はサーバとのやりとりをせずにクライアントが持つキャッシュをそのまま表示するので
//セッションをクリアしても、ブラウザの戻るボタンを押されるとセッションは空なのに値が表示されてしまう。
//解析ツールでHTTP通信みても通信されないことがわかる
//この問題はi1810aサイトのすべてで問題になっている。ログアウトした後でもブラウザ戻るボタンで中身を表示できてしまうなど
//こういった問題への対策は、いくつか考えられる
//
//(1)ブラウザにBackボタンを表示しないようにする
//(2)ブラウザのキャッシュが無効になるように制御する
//(3)Backボタンで画面が戻されても、不整合が起こらないようにアプリケーションを設計する
//ブラウザのキャッシュ機能を無効にするのが良い、開発中はキャッシュ機能無効化を推奨

public class SettlementConfirmAction extends ActionSupport implements SessionAware{

	private Collection<String> checkList;
	private Map<String, Object> session;

	@SuppressWarnings("unchecked")
	public String execute() {

		String result = ERROR;
		boolean loginFlg = false;
		int logined = 2;
		String userId="";

		if(session.get("checkListErrorMessageList") != null && checkList != null) {
			session.remove("checkListErrorMessageList");
		}
		if(!(session.get("userId") == null)){
			userId = session.get("userId").toString();
		}
		if(!((session.get("logined")) == null)){
			String loginedStr = session.get("logined").toString();
			logined = Integer.valueOf(loginedStr);
		}

		List<CartInfoDTO> cartInfoDtoList = new ArrayList<>();

		if(session.containsKey("cartInfoDtoList")){

			cartInfoDtoList = (ArrayList<CartInfoDTO>) session.get("cartInfoDtoList");
		}else{
			return "injustice";
		}


		if(logined == 1){
			DestinationInfoDAO destinationInfoDAO = new DestinationInfoDAO();
			List<DestinationInfoDTO> destinationInfoDtoList = new ArrayList<>();
			try {
				destinationInfoDtoList = destinationInfoDAO.getDestinationInfo(userId);
				Iterator<DestinationInfoDTO> iterator = destinationInfoDtoList.iterator();
				if(!(iterator.hasNext())) {
					destinationInfoDtoList = null;
				}
				session.put("destinationInfoDtoList", destinationInfoDtoList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//Listを毎回初期化しないと商品履歴を表示した際に過去に購入した商品が何度も表示される
		List<PurchaseHistoryInfoDTO> purchaseHistoryInfoDtoList = new ArrayList<PurchaseHistoryInfoDTO>();

		for(CartInfoDTO dto:cartInfoDtoList){
			PurchaseHistoryInfoDTO purchaseHistoryInfoDTO = new PurchaseHistoryInfoDTO();
			purchaseHistoryInfoDTO.setUserId(userId);
			purchaseHistoryInfoDTO.setProductId(dto.getProductId());
			purchaseHistoryInfoDTO.setProductName(dto.getProductName());
			purchaseHistoryInfoDTO.setProductNameKana(dto.getProductNameKana());
			purchaseHistoryInfoDTO.setImageFilePath(dto.getImageFilePath());
			purchaseHistoryInfoDTO.setImageFileName(dto.getImageFileName());
			purchaseHistoryInfoDTO.setPrice(dto.getPrice());
			purchaseHistoryInfoDTO.setReleaseCompany(dto.getReleaseCompany());
			purchaseHistoryInfoDTO.setProductCount(dto.getProductCount());
			purchaseHistoryInfoDTO.setSubtotal(dto.getSubtotal());
			purchaseHistoryInfoDtoList.add(purchaseHistoryInfoDTO);
		}
		for(PurchaseHistoryInfoDTO dto:purchaseHistoryInfoDtoList) {
			System.out.println("ProductName : "+dto.getProductName());
		}
		session.put("purchaseHistoryInfoDtoList", purchaseHistoryInfoDtoList);
		if(logined == 0){
			loginFlg = true;
			session.put("loginFlg", loginFlg);
			session.remove("loginIdMessage");
			session.remove("loginPassMessage");
			session.remove("loginErrorMessageList");
			result = ERROR;
		}else {
			session.put("loginFlg", loginFlg);
			result = SUCCESS;
		}
		return result;
	}

	public Collection<String> getCheckList() {
		return checkList;
	}
	public void setCheckList(Collection<String> checkList) {
		this.checkList = checkList;
	}
	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}