//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package csdt;

import com.fatwire.wem.sso.SSO;
import com.fatwire.wem.sso.SSOException;
import com.fatwire.wem.sso.SSOSession;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TicketMaster {
    private static String currentTicket;

    private TicketMaster() {
    }

    public static String getTicket() throws SSOException {
        TicketParams params = new TicketParams();
        params.setUrl(CSDPUtil.getBaseUrl());
        params.setUsername(CSDPUtil.getUserName());
        params.setPassword(CSDPUtil.getPassword());
        return getTicket(params);
    }

    public static String getTicket(TicketParams params) throws SSOException {
        String currentTicket = getCurrentTicket();
        return getValidTicket(currentTicket, params, true);
    }

    protected static String getValidTicket(String ticket, TicketParams params, boolean setIntoPlugin) throws SSOException {
        boolean valid = isValid(ticket, params);
        if(valid) {
            return ticket;
        } else {
            String tkt = getNewTicket(params);
            if(setIntoPlugin) {
                setCurrentTicket(tkt);
            }

            return tkt;
        }
    }

    private static void setCurrentTicket(String ticket) {
        //Activator.getDefault().setTix(ticket);
        currentTicket=ticket;
    }

    private static String getCurrentTicket() {
        //return Activator.getDefault().getTix();
        return currentTicket;
    }

    public static String getNewTicket(TicketParams params) throws SSOException {
        SSOSession sess = SSO.getSSOSession(params.getUrl());
        return sess.getMultiTicket(params.getUsername(), params.getPassword());
    }

    public static boolean isValid(String multiticket, TicketParams params) throws SSOException {
        if(multiticket == null) {
            return false;
        } else {
            String strValidationUrl = params.getUrl() + "/" + "Satellite" + "?" + CSDPUtil.buildQuery(new String[][]{{"pagename", "fatwire/wem/ui/Ping"}, {"multiticket", multiticket}});
            URL validationUrl = null;

            try {
                validationUrl = new URL(strValidationUrl);
            } catch (MalformedURLException var20) {
                throw new IllegalArgumentException(var20);
            }

            InputStream is1 = null;
            HttpURLConnection con1 = null;

            boolean e1;
            try {
                con1 = (HttpURLConnection)validationUrl.openConnection();
                con1.setConnectTimeout(1500);
                con1.setRequestMethod("GET");
                con1.setRequestProperty("X-CSRF-Token", multiticket);
                con1.setRequestProperty("Pragma", "auth-redirect=false");
                con1.connect();
                is1 = con1.getInputStream();
                if(con1.getResponseCode() != 200) {
                    throw new SSOException("Could not get a ticket to perform this operation");
                }

                e1 = true;
            } catch (IOException var22) {
                IOException e = var22;

                try {
                    if(con1.getResponseCode() == 403) {
                        e1 = false;
                        return e1;
                    }

                    throw new SSOException(e);
                } catch (IOException var21) {
                    throw new SSOException(var22);
                }
            } finally {
                if(is1 != null) {
                    try {
                        is1.close();
                    } catch (IOException var19) {
                        //Log.error("Could not close resource", var19);
                    }
                }

                con1.disconnect();
            }

            return e1;
        }
    }

    public static final class TicketParams {
        private String username;
        private String password;
        private String url;
        private String projectName;

        public TicketParams() {
        }

        public final String getUsername() {
            return this.username;
        }

        public final void setUsername(String username) {
            this.username = username;
        }

        public final String getPassword() {
            return this.password;
        }

        public final void setPassword(String password) {
            this.password = password;
        }

        public final String getUrl() {
            return this.url;
        }

        public final void setUrl(String url) {
            this.url = url;
        }

        public final void setUrl(String host, String port, String context) {
            this.url = CSDPUtil.getBaseUrl(host, port, context);
        }

        public final String getProjectName() {
            return this.projectName;
        }

        public final void setProjectName(String projectName) {
            this.projectName = projectName;
        }
    }
}
