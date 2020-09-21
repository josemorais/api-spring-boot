package com.spring.agendalive.controller;

import com.spring.agendalive.document.LiveDocument;
import com.spring.agendalive.service.LiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/lives")
public class LiveController {

	@Autowired
	LiveService liveService;

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml" })
	public ResponseEntity<Page<LiveDocument>> getAllLives(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
			@RequestParam(required = false) String flag) {
		Page<LiveDocument> livePage = liveService.findAll(pageable, flag);
		if (livePage.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(livePage, HttpStatus.OK);
		}
	}

	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml" })
	public ResponseEntity<LiveDocument> getOneLive(@PathVariable String id) {
		Optional<LiveDocument> liveO = liveService.findById(id);
		if (!liveO.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(liveO.get(), HttpStatus.OK);
		}
	}

	@PostMapping
	public ResponseEntity<LiveDocument> saveLive(@RequestBody @Valid LiveDocument live) {
		live.setRegistrationDate(LocalDateTime.now());
		return new ResponseEntity<>(liveService.save(live), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteLive(@PathVariable String id) {
		Optional<LiveDocument> liveO = liveService.findById(id);
		if (!liveO.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			liveService.delete(liveO.get());
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<LiveDocument> updateLive(@PathVariable String id,
			@RequestBody @Valid LiveDocument liveDocument) {
		Optional<LiveDocument> liveO = liveService.findById(id);
		if (!liveO.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			liveDocument.setId(liveO.get().getId());
			return new ResponseEntity<>(liveService.save(liveDocument), HttpStatus.OK);
		}
	}
}
