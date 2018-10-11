package it.uniroma2.sdccproject.dao;

import it.uniroma2.sdccproject.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NodeDao extends JpaRepository<Node,Long> {

    Node getByIpAddrEquals(String ipAddr);

}
