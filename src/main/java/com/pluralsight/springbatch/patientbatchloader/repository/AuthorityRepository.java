package com.pluralsight.springbatch.patientbatchloader.repository;

import com.pluralsight.springbatch.patientbatchloader.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
