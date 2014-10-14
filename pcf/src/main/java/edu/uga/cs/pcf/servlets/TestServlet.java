package edu.uga.cs.pcf.servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.pcf.base.PcfLinker;

@WebServlet("/Test")
public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 8555887631707602493L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        InputStream pcl = PcfLinker.getDefaultPcl();
        InputStream switchyard = PcfLinker.getDefaultSwitchyard();
        PcfLinker linker = new PcfLinker(pcl, switchyard);
        linker.initialize();
        linker.link();
    }
}
