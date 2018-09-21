package it.uniroma2.sdccproject.rest;

import it.uniroma2.sdccproject.controller.NodeController;
import it.uniroma2.sdccproject.entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "node")
@CrossOrigin
public class NodeRest {

    @Autowired
    private NodeController nodeController;

    @RequestMapping(path = "getNode", method = RequestMethod.GET)
    public ResponseEntity<Node> findNode() {
        Node node = nodeController.findNode();
        if(node == null)
            return new ResponseEntity<>(node, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }
}
