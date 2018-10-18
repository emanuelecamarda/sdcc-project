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

    /**
     * API Rest to create a new node.
     * @param node to create
     * @return the new node and the status 201 Created
     */
    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<Node> createNode(@RequestBody Node node) {
        Node newNode = nodeController.createNode(node);
        return new ResponseEntity<>(newNode, HttpStatus.CREATED);
    }

    /**
     * API Rest to update a node. Return status 200 OK if success, 404 Not Found if the updated node doesn't exists in
     * DB
     * @param id of node to update
     * @param node with the information to update
     * @return the node updated
     */
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

    /**
     * API Rest to find a node by id.
     * @param id of node
     * @return the node founded. Status 200 OK if founded, 404 Not Found otherwise
     */
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

    /**
     * API Rest to delete a node.
     * @param id of node
     * @return true with status 200 OK if deleted, false with status 404 Not Found otherwise.
     */
    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteNode(@PathVariable Long id) {
        boolean deleted = nodeController.deleteNode(id);
        return new ResponseEntity<>(deleted, deleted ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /**
     * API Rest to find all node reachable.
     * @return list of all node reachable with status 200 OK
     * @throws IOException
     */
    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<List<Node>> findAllNode() throws IOException {
        List<Node> nodes = nodeController.findAllNode();
        return new ResponseEntity<>(nodes, HttpStatus.OK);
    }

    /**
     * API Rest for the registration of a new node.
     * @param request of node to obtain its ip address
     * @return node registrated with status 201 Created
     */
    @RequestMapping(path = "registration", method = RequestMethod.POST)
    public ResponseEntity<Node> registerNode(HttpServletRequest request) {
        System.out.println("Registering node: " + request.getRemoteAddr());
        Node nodeToRegister = new Node(request.getRemoteAddr());
        Node newNode = nodeController.createNode(nodeToRegister);
        System.out.println("Registered");
        return new ResponseEntity<>(newNode, HttpStatus.CREATED);
    }

    /**
     * API Rest to find a node with random policy.
     * @return node founded with status 200 OK, null with status 404 Not Found otherwise
     * @throws IOException
     */
    @RequestMapping(path = "random", method = RequestMethod.GET)
    public ResponseEntity<Node> findNodeRandom() throws IOException {
        Node node = nodeController.findNodeRandom();
        if(node == null)
            return new ResponseEntity<>(node, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    /**
     * API Rest to find the closest node to client.
     * @param request of client to obtain its ip address
     * @return node founded with status 200 OK, null with status 404 Not Found otherwise
     * @throws IOException
     */
    @RequestMapping(path = "distance", method = RequestMethod.GET)
    public ResponseEntity<Node> findNodeByDistance(HttpServletRequest request) throws IOException {
        String clientIp = request.getRemoteAddr();
        Node node = nodeController.findNodeByDistance(clientIp);
        if (node == null)
            return new ResponseEntity<>(node, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }
}