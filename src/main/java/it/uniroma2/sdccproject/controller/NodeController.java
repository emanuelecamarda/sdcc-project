package it.uniroma2.sdccproject.controller;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import it.uniroma2.sdccproject.dao.NodeDao;
import it.uniroma2.sdccproject.entity.Node;
import it.uniroma2.sdccproject.exception.NotFoundEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Service
public class NodeController {

    @Autowired
    private NodeDao nodeDao;

    public Node findNode(Long id) throws NotFoundEntityException {
        if (!nodeDao.existsById(id))
            throw new NotFoundEntityException();
        return nodeDao.getOne(id);
    }

    @Transactional
    public Node createNode(@NotNull Node node) {
        Node node2 = nodeDao.getByIpAddrEquals(node.getIpAddr());
        if (node2 != null)
            return node2;
        return nodeDao.save(node);
    }

    @Transactional
    public Node updateNode(@NotNull Long id, @NotNull Node node) throws NotFoundEntityException {
        Node nodeToUpdate = nodeDao.getOne(id);
        if (nodeToUpdate == null)
            throw new NotFoundEntityException();
        nodeToUpdate.update(node);

        return nodeDao.save(nodeToUpdate);
    }

    @Transactional
    public boolean deleteNode(@NotNull Long id) {
        if (!nodeDao.existsById(id)) {
            return false;
        }

        nodeDao.deleteById(id);
        return true;
    }

    public List<Node> findAllNode() throws IOException {
        List<Node> nodes = nodeDao.findAll();
        for (int i = 0; i < nodes.size(); i++) {
            InetAddress inet = InetAddress.getByName(nodes.get(i).getIpAddr());
            if (!inet.isReachable(5000)) {
                System.out.println("Node: " + nodes.get(i).getIpAddr() + " not reachable. It'll be removed from " +
                        "the list");
                deleteNode(nodes.get(i).getId());
                nodes.remove(i);
            }
        }
        if (nodes.isEmpty())
            return null;
        return  nodeDao.findAll();
    }

    public Node findNodeRandom() throws IOException {
        List<Node> nodes = nodeDao.findAll();
        if (nodes.isEmpty())
            return null;
        int i = -1;
        boolean find = false;
        while (!find) {
            if (nodes.isEmpty())
                return null;
            i = (int) Math.floor(Math.random() * nodes.size());

            System.out.println("Sent to client node: " + nodes.get(i).getIpAddr());

            InetAddress inet = InetAddress.getByName(nodes.get(i).getIpAddr());
            if (inet.isReachable(5000))
                find = true;
            else {
                System.out.println("Node: " + nodes.get(i).getIpAddr() + "not reachable. It'll be removed from " +
                        "the list");
                deleteNode(nodes.get(i).getId());
                nodes.remove(i);
            }
        }
        return nodes.get(i);
    }

    public Node findNodeByDistance(@NotNull String clientIp) throws IOException {
        List<Node> nodes = nodeDao.findAll();
        if (nodes.isEmpty())
            return null;
        double bestDistance = -1, distance;
        int bestNode = 0;
        LookupService lookupService = new LookupService("/home/ec2-user/data/GeoLiteCity.dat");
        Location locationClient = lookupService.getLocation(clientIp);
        System.out.println("Location of the client: " + locationClient.city);
        Location locationNode;
        boolean find = false;
        while (!find) {
            if (nodes.isEmpty())
                return null;
            for (int i = 0; i < nodes.size(); i++) {
                locationNode = lookupService.getLocation(nodes.get(i).getIpAddr());
                distance = locationClient.distance(locationNode);
                if (bestDistance == -1 || bestDistance > distance) {
                    bestDistance = distance;
                    bestNode = i;
                }
            }
            System.out.println("Sent to client nearest node: " + nodes.get(bestNode).getIpAddr());

            InetAddress inet = InetAddress.getByName(nodes.get(bestNode).getIpAddr());
            if (inet.isReachable(5000))
                find = true;
            else {
                System.out.println("Node: " + nodes.get(bestNode).getIpAddr() + "not reachable. It'll be removed " +
                        "from the list");
                deleteNode(nodes.get(bestNode).getId());
                nodes.remove(bestNode);
            }
        }
        return nodes.get(bestNode);
    }
}
