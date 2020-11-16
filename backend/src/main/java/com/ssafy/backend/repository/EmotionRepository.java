package com.ssafy.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.backend.model.Emotion;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Long> {
	Emotion findByNum(Long num);
	List<Emotion> findByAngry(String angry);
}
