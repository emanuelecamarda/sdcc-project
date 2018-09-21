package it.uniroma2.sdccproject.dao;

import it.uniroma2.sdccproject.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NodeDao extends JpaRepository<Node,Long> {

}
