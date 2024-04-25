package tests;

import models.publication;
import services.publicationservice;

import java.sql.SQLException;

public class testing {
    public static void main(String[] args) throws SQLException {
        publicationservice ps=new publicationservice();
        publication p=new publication("azeaze","ty","mahboul-65dc5c3f2d878.jpg");
        for (publication pub : ps.read())
        {
            System.out.println(pub.getId()+":"+pub.getTitre()+":"+pub.getContenu());
        }
        ps.create(p);
        for (publication pub : ps.read())
        {
            System.out.println(pub.getId()+":"+pub.getTitre()+":"+pub.getContenu());

        }

    }
}
