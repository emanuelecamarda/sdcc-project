package it.uniroma2.sdccproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "nodes")
/* This class rappresents a Fog node */
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "node_id")
    private Long id;
    @NaturalId
    // The public Ip address of the node
    private String ipAddr;

    public Node(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public void update(Node node) {
        if(node.ipAddr !=null)
            this.ipAddr = node.ipAddr;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public Long getId() {
        return id;
    }
}
