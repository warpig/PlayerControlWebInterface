package cmtc.rdms.webgui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmtc.cn.tasks.CNTaskPlayerStateManager;
import cmtc.pass.CisPassException;
import cmtc.rdms.entities.DisForce;
import cmtc.rdms.entities.DisPredefinedEntity;
import cmtc.rdms.entities.PlayerState;
import cmtc.test.rdms.PlayerSimulator;

public class RDMSPlayerController {
	public static CNTaskPlayerStateManager psm;
	private static final Logger log = cmtc.pass.CisPassFactory
			.getSimpleLogger("RDMSWEBSERVER");
	private boolean debug = true;

	// public static final String SRCH_MARKING_TEXT = "srch_marking_text";
	// public static final String SRCH_FORCE = "srch_force_id";
	// public static final String SRCH_TYPE = "srch_type_id";

	public RDMSPlayerController() {
		try {
			log.config("Starting controller " + this.getClass().getName());
			if (debug)
				log.setLevel(Level.FINE);
			psm = new CNTaskPlayerStateManager();
			if(debug){
			psm.remove(psm.getPlayerStateList());
			psm.addUpdate(PlayerSimulator.getVariedFakePlayers());
			}
		} catch (CisPassException e) {
			e.printStackTrace();
		}
	}

	public int getCurrentPlayerCount() {
		return psm.getCurrentPlayerCount();
	}

	public List<PlayerState> getPlayers() {
		return psm.getPlayerStateList();
	}

	/**
	 * Returns a filtered list based on the parms. If a parameter is null it
	 * will not filter based on that value.
	 * 
	 * @param entityIdStr
	 *            return players that contain the string in their entity id.
	 *            This string must be a number to be meaningful.
	 * @param marking_text
	 *            filter on marking_text. Case insensitive.
	 * @param force
	 *            return players of this force type.
	 * @param type
	 *            return players of this predefined type.
	 * @return
	 */
	public synchronized List<PlayerState> getPlayers(String entityIdStr,
			String marking_text, DisForce force, DisPredefinedEntity type) {
		ArrayList<PlayerState> r = new ArrayList<PlayerState>();
		// Walk through list and only add players if they did not get flagged to
		// not add.
		for (PlayerState p : psm.getPlayerStateList()) {
			boolean doAdd = true;
			if (entityIdStr != null
					&& !Integer.toString(p.getEntityId()).contains(entityIdStr)) {
				doAdd = false;
				if (debug)
					log.fine("Player filtered because entity id "
							+ p.getEntityId() + " does not contain "
							+ entityIdStr);
			} else if (marking_text != null
					&& !marking_text.equals("")
					&& !p.getMarkingText().toLowerCase().contains(
							marking_text.toLowerCase())) {
				doAdd = false;
				if (debug)
					log.fine("Player filtered because markingtext "
							+ p.getMarkingText() + " does not contain "
							+ marking_text);
			} else if (force != null  && !p.getDisForce().equals(force)) {
				doAdd = false;
				log.fine("Player filtered because force id " + p.getForceId()
						+ " != " + force.getForceId());
			} else if (type != null && !p.getDisPredefinedEntity().equals(type)) {
				doAdd = false;
				log.fine("Player filtered because predefinedEntityId id "
						+ p.getPredefinedEntityId() + " != "
						+ type.getPredefinedEntityId());
			}
			if (doAdd) {
				r.add(p);
				if (debug)
					log.fine("Player was added to the list: " + p.toString());
			} else {
				if (debug)
					log.fine("Player p was filtered out: " + p.toString());
			}
		}
		return r;
	}

	public List<DisForce> getForceList() {
		return DisForce.getForceList();
	}

	public List<DisPredefinedEntity> getTypesList() {
		return DisPredefinedEntity.getTypesList();
	}

	public void log(Level level, String msg) {
		log.log(level, msg);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isDebug() {
		return debug;
	}

}
