package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.StatusItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusItemRepository extends JpaRepository<StatusItem, Long> {
}
