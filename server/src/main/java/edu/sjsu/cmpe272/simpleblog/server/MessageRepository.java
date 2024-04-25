package edu.sjsu.cmpe272.simpleblog.server;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



public interface MessageRepository extends JpaRepository<MessageHandler, Long> {
    List<MessageHandler> findAllByOrderByMessageIdDesc();
    List<MessageHandler> findAllByOrderByMessageIdAsc();
}