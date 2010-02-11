package cmtc.rdms.webgui;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cmtc.cn.entities.DisForce;
import cmtc.cn.entities.DisPredefinedEntity;
import cmtc.cn.entities.PlayerState;
import cmtc.cn.tasks.CNTaskPlayerStateManager;
import cmtc.cn.tasks.TaskHealthManager;
import cmtc.pass.CisPassException;
import cmtc.pass.admin.MxObjectHealth;
import cmtc.test.cn.PlayerSimulator;

public class RDMSPlayerController {
	private static CNTaskPlayerStateManager psm = null;
	private static TaskHealthManager thm = null;
	private static final Logger log = cmtc.pass.CisPassFactory
			.getSimpleLogger("RDMSWEBSERVER");
	private boolean debug = false;
	private boolean loadTestData = true;
	public static final String sessionErrorString = "sessionErrorString";
	public static final String sessionMessageString = "sessionMessageString";
	public static final String sessionPlayer = "sessionPlayer";




	// public static final String SRCH_MARKING_TEXT = "srch_marking_text";
	// public static final String SRCH_FORCE = "srch_force_id";
	// public static final String SRCH_TYPE = "srch_type_id";

	public RDMSPlayerController() {
		try {
			log.config("Starting controller " + this.getClass().getName());
			if (debug)
				log.setLevel(Level.CONFIG);

			// Player state manager
			if (psm == null) {
				psm = new CNTaskPlayerStateManager();
				psm.setLoggingLevel(Level.SEVERE);
				psm.setTaskName("Webserver PSM");
				psm.setReportingInterval(5000);
				psm.startHealthReportPublisher();
			}
			// task health manager...
			if (thm == null) {
				thm = new TaskHealthManager();
				thm.setLoggingLevel(Level.SEVERE);
				thm.setTaskName("Webserver TaskHealth");
				thm.setReportingInterval(5000);
				thm.startHealthReportPublisher();
			}
			if (loadTestData) {
				psm.remove(psm.getPlayerStateList());
				psm.addUpdate(PlayerSimulator.getVariedFakePlayers(20));
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
	 *            filter on marking_text. Case insensitive. Matches on partial.
	 * @param force
	 *            return players of this force type.
	 * @param type
	 *            return players of this predefined type.
	 * @return
	 */
	public synchronized List<PlayerState> getPlayers(String entityIdStr,
			String marking_text, DisForce force, DisPredefinedEntity type,
			boolean allowReportingRadios, boolean allowNonReportingRadios) {
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
			} else if (force != null && !p.getDisForce().equals(force)) {
				doAdd = false;
				if(debug)log.fine("Player filtered because force id " + p.getForceId()
						+ " != " + force.getForceId());
			} else if (type != null
					&& !p.getDisPredefinedEntityFromId().equals(type)) {
				doAdd = false;
				if (debug)
					log.fine("Player filtered because predefinedEntityId id "
							+ p.getPredefinedEntityId() + " != "
							+ type.getPredefinedEntityId());
			}
			if (p.isInConectivity()) {
				if (allowReportingRadios == false) {
					doAdd = false;
					if (debug)
						log
								.fine("Player filtered because allowReportingRadios = "
										+ allowReportingRadios
										+ " but player conectivity is "
										+ p.isInConectivity());
				}
			} else {
				if (allowNonReportingRadios == false) {
					doAdd = false;
					log
							.fine("Player filtered because allowNonReportingRadios = "
									+ allowNonReportingRadios
									+ " but player conectivity is "
									+ p.isInConectivity());
				}
			}
			if (doAdd) {
				r.add(p);
				if (debug)
					log.fine("Player was added to the list: " + p.toString());
			} else {
				if (false || debug)
					log.fine("Player p was filtered out: " + p.toString());
			}
		}
		return r;
	}




	/**
	 * add or update a player based on values.
	 * 
	 * @param entityId
	 * @param marking_text
	 * @param force
	 * @param type
	 * @return true if add or delete performed, false if not.
	 */
	public void addUpdate(PlayerState player) {
		psm.addUpdate(player);
	}




	public List<DisForce> getForceList() {
		return DisForce.getForceList();
	}


	public List<MxObjectHealth> getTaskHealthList(){
		return this.thm.getMxObjectHealthList();
	}


	public List<DisPredefinedEntity> getTypesList() {
		return DisPredefinedEntity.getTypesList();
	}




	public String getEditPlayerURL(int entityId) {
		return "<a href=\"addplayer?entity_id_text=" + entityId + "\">"
				+ entityId + "</a>";
	}




	public PlayerState getPlayerState(int entityId) {
		return psm.getPlayerStateFromEntityId(entityId);
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




	/**
	 * @param disForceID
	 *            String for a DisForce id.
	 * @return The DisForce for the id or null.
	 */
	public DisForce getForce(String disForceID) {
		DisForce r = null;
		if (disForceID != null) {
			try {
				int forceid = Integer.parseInt(disForceID);
				r = DisForce.getDisForce(forceid);
			} catch (NumberFormatException e) {
				log.warning("Bad force id passed from view? " + disForceID);
			}
		}
		return r;
	}




	/**
	 * @param DisPredefinedEntityId
	 *            String for a DisPredefinedEntity id.
	 * @return The DisPredefinedEntity for the id or null.
	 */
	public DisPredefinedEntity getType(String DisPredefinedEntityId) {
		DisPredefinedEntity r = null;
		if (DisPredefinedEntityId != null) {
			try {
				int forceid = Integer.parseInt(DisPredefinedEntityId);
				r = DisPredefinedEntity.queryById(forceid);
			} catch (NumberFormatException e) {
				log.warning("Bad DisPredefinedEntity id passed from view? "
						+ DisPredefinedEntityId);
			}
		}
		return r;
	}




	public boolean isValidEntityId(String entity_id_text) {
		boolean r = true;
		try {
			int entity_id = Integer.parseInt(entity_id_text);
			if (entity_id < 0 || entity_id > 10000) {
				log.fine("tried to use an entity id outside range: "
						+ entity_id);
				r = false;
			}
		} catch (NumberFormatException e) {
			r = false;
			log.fine("tried to use non-number: " + entity_id_text);
		}
		return r;
	}

}
