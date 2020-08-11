package com.spring.agendalive.service;

import com.spring.agendalive.document.LiveDocument;
import com.spring.agendalive.repository.LiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LiveService {

    @Autowired
    LiveRepository liveRepository;

    @Autowired
    private SimpMessagingTemplate template;

    public Page<LiveDocument> findAll(Pageable pageable, String flag) {
        if (flag != null && flag.equals("next")) {
            return liveRepository.findByLiveDateAfterOrderByLiveDateAsc(LocalDateTime.now(), pageable);
        } else if (flag != null && flag.equals("previous")) {
            return liveRepository.findByLiveDateBeforeOrderByLiveDateDesc(LocalDateTime.now(), pageable);
        } else {
            return liveRepository.findAll(pageable);
        }
    }

    public Optional<LiveDocument> findById(String id) {
        return liveRepository.findById(id);
    }

    @Async
    public LiveDocument save(LiveDocument liveDocument) {
        template.convertAndSend("/liveProcessor", liveDocument);
        return liveRepository.save(liveDocument);
    }

    public void delete(LiveDocument liveDocument) {
        liveRepository.delete(liveDocument);
    }
}
