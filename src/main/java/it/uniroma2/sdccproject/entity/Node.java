package it.uniroma2.sdccproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "nodes")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "node_id")
    private Long id;
    private String ipAddr;

    public Node(String ipAddr) {
        this.ipAddr = ipAddr;
    }
}
