package it.uniroma2.sdccproject.rest;

import it.uniroma2.sdccproject.exception.NotFoundEntityException;
import it.uniroma2.sdccproject.controller.NodeController;
import it.uniroma2.sdccproject.entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "node")
@CrossOrigin
public class NodeRest {

    @Autowired
    private NodeController nodeController;

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<Node> createNode(@RequestBody Node node) {
        Node newNode = nodeController.createNode(node);
        return new ResponseEntity<>(newNode, HttpStatus.CREATED);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Node> updateNode(@PathVariable Long id, @RequestBody Node node) {
        Node nodeUpdated = null;
        try {
            nodeUpdated = nodeController.updateNode(id, node);
        } catch (NotFoundEntityException e) {
            return new ResponseEntity<>(nodeUpdated, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(nodeUpdated, HttpStatus.OK);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Node> findNode(@PathVariable Long id) {
        Node node = null;
        try {
            node = nodeController.findNode(id);
        } catch (NotFoundEntityException e) {
            e.printStackTrace();
        }
        if(node == null)
            return new ResponseEntity<>(node, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteNode(@PathVariable Long id) {
        boolean deleted = nodeController.deleteNode(id);
        return new ResponseEntity<>(deleted, deleted ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<List<Node>> findAllNode() throws IOException {
        List<Node> nodes = nodeController.findAllNode();
        return new ResponseEntity<>(nodes, HttpStatus.OK);
    }

    @RequestMapping(path = "registration", method = RequestMethod.POST)
    public ResponseEntity<Node> registerNode(HttpServletRequest request) {
        System.out.println("Registering node: " + request.getRemoteAddr());
        Node nodeToRegister = new Node(request.getRemoteAddr());
        Node newNode = nodeController.createNode(nodeToRegister);
        System.out.println("Registered");
        return new ResponseEntity<>(newNode, HttpStatus.CREATED);
    }

    @RequestMapping(path = "random", method = RequestMethod.GET)
    public ResponseEntity<Node> findNodeRandom() throws IOException {
        Node node = nodeController.findNodeRandom();
        if(node == null)
            return new ResponseEntity<>(node, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    @RequestMapping(path = "distance", method = RequestMethod.GET)
    public ResponseEntity<Node> findNodeByDistance(HttpServletRequest request) throws IOException {
        String clientIp = request.getRemoteAddr();
        Node node = nodeController.findNodeByDistance(clientIp);
        if (node == null)
            return new ResponseEntity<>(node, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }
}