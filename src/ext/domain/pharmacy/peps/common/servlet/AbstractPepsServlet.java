/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Receive a request from VistA, retrieve the XML request parameter, and forward it on to a subclass. Then write the String
 * return value to the response.
 */
public abstract class AbstractPepsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TEXT_XML_CONTENT_TYPE = "text/xml";
    private static final String XML_REQUEST_PARAMETER = "xmlRequest";
    /**
     * Delegate DELETE requests to {@link #doService(ServletRequest, ServletResponse)}.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if error
     * @throws IOException if error
     * 
     * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        doService(request, response);
    }
    /**
     * Delegate GET requests to {@link #doService(ServletRequest, ServletResponse)}.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if error
     * @throws IOException if error
     * 
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        doService(request, response);
    }
    /**
     * Delegate POST requests to {@link #doService(ServletRequest, ServletResponse)}.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if error
     * @throws IOException if error
     * 
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        doService(request, response);
    }
    /**
     * Delegate PUT requests to {@link #doService(ServletRequest, ServletResponse)}.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if error
     * @throws IOException if error
     * 
     * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        doService(request, response);
    }
    /**
     * Get the XML request parameter from the HttpServletRequest and forward it to the subclass via the
     * {@link #getResponse(String)} method.
     * 
     * Then write the String return value to the response.
     * 
     * @param request HttpServletRequest from VistA
     * @param response HttpServletResponse with XML response for VistA
     * @throws IOException If error processing request or writing response
     * 
     * @see javax.servlet.Servlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    private void doService(ServletRequest request, ServletResponse response) throws IOException {
        response.setContentType(TEXT_XML_CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println(getResponse(request.getParameter(XML_REQUEST_PARAMETER)));
        out.flush();
        out.close();
    }
    /**
     * Handle the XML Request from VistA.
     * 
     * @param xmlRequest String request from VistA
     * @return String response for VistA
     */
    protected abstract String getResponse(String xmlRequest);
