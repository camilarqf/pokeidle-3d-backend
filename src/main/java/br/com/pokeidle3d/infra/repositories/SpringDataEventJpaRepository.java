package br.com.pokeidle3d.infra.repositories;

import br.com.pokeidle3d.infra.persistence.EventJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataEventJpaRepository extends JpaRepository<EventJpaEntity, Long> {
}
