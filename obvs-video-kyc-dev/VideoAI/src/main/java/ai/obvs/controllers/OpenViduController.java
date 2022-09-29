package ai.obvs.controllers;

import ai.obvs.services.impl.OpenViduService;
import io.openvidu.java.client.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@RestController
@RequestMapping("/v1/openvidu")
public class OpenViduController {

	private OpenViduService openViduService;

	public  OpenViduController(OpenViduService openViduService){
		this.openViduService = openViduService;
	}
	/*******************/
	/*** Session API ***/
	/*******************/

	@RequestMapping(value = "/get-token", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> getToken(@RequestBody String sessionNameParam) throws ParseException {
		return openViduService.getToken(sessionNameParam);
	}

	@RequestMapping(value = "/remove-user", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> removeUser(@RequestBody String sessionNameToken) throws Exception {
		return openViduService.removeUser(sessionNameToken);
	}

	@RequestMapping(value = "/close-session", method = RequestMethod.DELETE)
	public ResponseEntity<JSONObject> closeSession(@RequestBody String sessionName) throws Exception {
		return openViduService.closeSession(sessionName);
	}

	@RequestMapping(value = "/fetch-info", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> fetchInfo(@RequestBody String sessionName) {
		return openViduService.fetchInfo(sessionName);
	}

	@RequestMapping(value = "/fetch-all", method = RequestMethod.GET)
	public ResponseEntity<?> fetchAll() {
		return openViduService.fetchAll();
	}

	@RequestMapping(value = "/force-disconnect", method = RequestMethod.DELETE)
	public ResponseEntity<JSONObject> forceDisconnect(@RequestBody String sessionName) {
		return openViduService.forceDisconnect(sessionName);
	}

	@RequestMapping(value = "/force-unpublish", method = RequestMethod.DELETE)
	public ResponseEntity<JSONObject> forceUnpublish(@RequestBody String sessionName) {
		return openViduService.forceUnpublish(sessionName);
	}

	/*******************/
	/** Recording API **/
	/*******************/

	@RequestMapping(value = "/recording/start", method = RequestMethod.POST)
	public ResponseEntity<?> startRecording(@RequestBody String param) throws ParseException {
		return openViduService.startRecording(param);
	}

	@RequestMapping(value = "/recording/stop", method = RequestMethod.POST)
	public ResponseEntity<?> stopRecording(@RequestBody String param) throws ParseException {
		return openViduService.stopRecording(param);
	}

	@RequestMapping(value = "/recording/delete", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteRecording(@RequestBody String param) throws ParseException {
		return openViduService.deleteRecording(param);
	}

	@RequestMapping(value = "/recording/get/{recordingId}", method = RequestMethod.GET)
	public ResponseEntity<?> getRecording(@PathVariable(value = "recordingId") String recordingId) {
		try {
			Recording recording = openViduService.getRecording(recordingId);
			return new ResponseEntity<>(recording, HttpStatus.OK);
		} catch (OpenViduJavaClientException | OpenViduHttpException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/recording/list", method = RequestMethod.GET)
	public ResponseEntity<?> listRecordings() {
		return openViduService.listRecordings();
	}

}
