package it.uniroma2.sdccproject.controller;

import it.uniroma2.sdccproject.dao.NodeDao;
import it.uniroma2.sdccproject.entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NodeController {

    @Autowired
    private NodeDao nodeDao;

    public Node findNode() {
        return nodeDao.getOne((long) 1);
    }
}
