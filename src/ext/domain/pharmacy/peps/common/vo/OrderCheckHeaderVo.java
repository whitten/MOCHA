/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
import java.math.BigInteger;
/**
 * 
 */
public class OrderCheckHeaderVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private String time = "";
    private BigInteger jobNumber = new BigInteger("0");
    private BigInteger duz = new BigInteger("0");
    private String userName = "";
    private BigInteger stationNumber = new BigInteger("0");
    private String serverName = "";
    private String ip = "";
    private String uci = "";
    private String namespace = "";
    private boolean pingOnly;
    /**
     * Empty constructor
     */
    public OrderCheckHeaderVo() {
        super();
    }
    /**
     * 
     * @return duz property
     */
    public BigInteger getDuz() {
        return duz;
    }
    /**
     * 
     * @param duz duz property
     */
    public void setDuz(BigInteger duz) {
        this.duz = duz;
    }
    /**
     * 
     * @return ip property
     */
    public String getIp() {
        return ip;
    }
    /**
     * 
     * @param ip ip property
     */
    public void setIp(String ip) {
        this.ip = ip;
    }
    /**
     * 
     * @return jobNumber property
     */
    public BigInteger getJobNumber() {
        return jobNumber;
    }
    /**
     * 
     * @param jobNumber jobNumber property
     */
    public void setJobNumber(BigInteger jobNumber) {
        this.jobNumber = jobNumber;
    }
    /**
     * 
     * @return namespace property
     */
    public String getNamespace() {
        return namespace;
    }
    /**
     * 
     * @param namespace namespace property
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    /**
     * 
     * @return serverName property
     */
    public String getServerName() {
        return serverName;
    }
    /**
     * 
     * @param serverName serverName property
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    /**
     * 
     * @return stationNumber property
     */
    public BigInteger getStationNumber() {
        return stationNumber;
    }
    /**
     * 
     * @param stationNumber stationNumber property
     */
    public void setStationNumber(BigInteger stationNumber) {
        this.stationNumber = stationNumber;
    }
    /**
     * 
     * @return time property
     */
    public String getTime() {
        return time;
    }
    /**
     * 
     * @param time time property
     */
    public void setTime(String time) {
        this.time = time;
    }
    /**
     * 
     * @return uci property
     */
    public String getUci() {
        return uci;
    }
    /**
     * 
     * @param uci uci property
     */
    public void setUci(String uci) {
        this.uci = uci;
    }
    /**
     * 
     * @return userName property
     */
    public String getUserName() {
        return userName;
    }
    /**
     * 
     * @param userName userName property
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     * 
     * @return pingOnly property
     */
    public boolean isPingOnly() {
        return pingOnly;
    }
    /**
     * 
     * @param pingOnly pingOnly property
     */
    public void setPingOnly(boolean pingOnly) {
        this.pingOnly = pingOnly;
    }
