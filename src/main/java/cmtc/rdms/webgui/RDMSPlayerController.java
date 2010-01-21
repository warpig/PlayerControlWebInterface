package cmtc.rdms.webgui;

import java.util.List;

import cmtc.cn.tasks.CNTaskPlayerStateManager;
import cmtc.pass.CisPassException;
import cmtc.rdms.entities.PlayerState;

public class RDMSPlayerController {
	public static  CNTaskPlayerStateManager psm;
	
	
	public  RDMSPlayerController(){
		try {
			System.out.println("======== Starting controller");
			psm = new CNTaskPlayerStateManager();
		} catch (CisPassException e) {
			e.printStackTrace();
		}
	} 
	
	
	public int getCurrentPlayerCount(){
		return psm.getCurrentPlayerCount();
	}
	
	public List<PlayerState> getPlayers(){
		return psm.getPlayerStateList();
	}

}
