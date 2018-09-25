package it.uniroma2.sdccproject.controller;

import it.uniroma2.sdccproject.dao.NodeDao;
import it.uniroma2.sdccproject.entity.Node;
import it.uniroma2.sdccproject.exception.NotFoundEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
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
        return nodeDao.save(node);
    }

    @Transactional
    public Node updateNode(@NotNull Long id, @NotNull Node node) throws NotFoundEntityException{
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

    public List<Node> findAllNode() {
        return  nodeDao.findAll();
    }
}
