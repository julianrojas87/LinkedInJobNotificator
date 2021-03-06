package org.telcomp.sbb;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.EventContext;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import net.java.slee.resource.http.events.HttpServletRequestEvent;

import org.telcomp.events.EndGetDataTelcoServiceEvent;
import org.telcomp.events.EndMediaCallTelcoServiceEvent;
import org.telcomp.events.EndSendEmailTelcoServiceEvent;
import org.telcomp.events.EndWSInvocatorEvent;
import org.telcomp.events.StartGetDataTelcoServiceEvent;
import org.telcomp.events.StartMediaCallTelcoServiceEvent;
import org.telcomp.events.StartSendEmailTelcoServiceEvent;
import org.telcomp.events.StartWSInvocatorEvent;

public abstract class LinkedInJobNotificatorCSSbb implements javax.slee.Sbb {
	
	private NullActivityFactory nullActivityFactory;
	private NullActivityContextInterfaceFactory nullACIFactory;
	
	//Control flow Activities
	ActivityContextInterface mainAci;
	ActivityContextInterface andSplit1Aci;
	ActivityContextInterface andSplit2Aci;
	//Control flow parameters
	static int mainControlFlow = 0;
	static int branchControlFlow1 = 0;
	static int branchControlFlow2 = 0;
	
	//Parameters obtained on invocation
	static String startpn0 = "userid";
	static String startpv0;
	
	//Web Services Invocation Variables
	//getLinkedInJobs0
	static HashMap<String, String> getLinkedInJobs0operationInputs = new HashMap<String, String>();
	static String getLinkedInJobs0wsdl = "http://192.168.190.55:8084/EWSApp-war/LinkedInWebService?wsdl";
	static String getLinkedInJobs0operation = "getLinkedInJobs";
	static String getLinkedInJobs0ipn0 = "idLinkedInUser";
	static String getLinkedInJobs0ipv0;
	static String getLinkedInJobs0opn0 = "return";
	static List<String> getLinkedInJobs0opv0 = new ArrayList<String>();
	//sendTwitterMessage0
	static HashMap<String, String> sendTwitterMessage0operationInputs = new HashMap<String, String>();
	static String sendTwitterMessage0wsdl = "http://192.168.190.55:8084/EWSApp-war/TwitterWebService?wsdl";
	static String sendTwitterMessage0operation = "sendTwitterMessage";
	static String sendTwitterMessage0ipn0 = "callee";
	static String sendTwitterMessage0ipv0;
	static String sendTwitterMessage0ipn1 = "text";
	static String sendTwitterMessage0ipv1;
	static String sendTwitterMessage0opn0 = "return";
	static String sendTwitterMessage0opv0;
	
	//Telco Services I/O Parameters
	//GetDataTelcoService0
	static String GetDataTelcoService0ip0;
	static String GetDataTelcoService0ip1 = "linkedinid"; //User defined input on creation time
	static String GetDataTelcoService0op0;
	//GetDataTelcoService1
	static String GetDataTelcoService1ip0;
	static String GetDataTelcoService1ip1 = "email"; //User defined input on creation time
	static String GetDataTelcoService1op0;
	//GetDataTelcoService2
	static String GetDataTelcoService2ip0;
	static String GetDataTelcoService2ip1 = "twitterid"; //User defined input on creation time
	static String GetDataTelcoService2op0;
	//SendEmailService0
	static String SendEmailService0ip0;
	static String SendEmailService0ip1;
	static String SendEmailService0ip2 = ""; //User defined input on creation time
	static boolean SendEmailService0op0;
	//GetDataTelcoService3
	static String GetDataTelcoService3ip0;
	static String GetDataTelcoService3ip1 = "sipuri"; //User defined input on creation time
	static String GetDataTelcoService3op0;
	//MediaCallTelcoService0
	static String MediaCallTelcoService0ip0;
	static String MediaCallTelcoService0ip1;
	static boolean MediaCallTelcoService0op0;
	
	static HttpServletResponse httpResponse;
	static ActivityContextInterface httpAci;
	

	public void onHttpGet (HttpServletRequestEvent event, ActivityContextInterface aci, EventContext context) {
		if(event.getRequest().getPathInfo().indexOf("LinkedInJobNotificator") >= 0){
			HttpServletResponse response = event.getResponse();
			httpResponse = response;
			httpAci = aci;
			this.setEventContext(context);
			
			context.suspendDelivery();
			
			//Initial parameter mapping for start place
			startpv0 = event.getRequest().getParameter(startpn0);
		
			//Initializing execution ACI
			this.mainAci = this.createNullActivityACI();
			this.mainAci.attach(this.sbbContext.getSbbLocalObject());
			
			//Invoking DataAccess Service on step 1
			mainControlFlow = 1;
			//Parameters mapping
			GetDataTelcoService0ip0 = startpv0;
			HashMap<String, Object> operationInputs = new HashMap<String, Object>();
			operationInputs.put("parameter", (String) GetDataTelcoService0ip1);
			operationInputs.put("identification", (String) GetDataTelcoService0ip0);
			StartGetDataTelcoServiceEvent getDataEvent = new StartGetDataTelcoServiceEvent(operationInputs);
			this.fireStartGetDataTelcoServiceEvent(getDataEvent, this.mainAci, null);
			System.out.println("StartGetData Event fired on execution step: "+mainControlFlow);
		} else {
			aci.detach(this.sbbContext.getSbbLocalObject());
		}
	}
	
	public void onEndGetDataTelcoServiceEvent(EndGetDataTelcoServiceEvent event, ActivityContextInterface aci){
		if(mainControlFlow == 1){
			System.out.println("EndGetData Event received on execution step: "+mainControlFlow);
			//Setting mainAci object to current state
			this.mainAci = aci;
			//Getting DataAccess Service response data on step 2
			GetDataTelcoService0op0 = event.getValue();
			
			//Invoking LinkedIn WS on step 2
			mainControlFlow = 2;
			//Parameters mapping
			getLinkedInJobs0ipv0 = GetDataTelcoService0op0;
			getLinkedInJobs0operationInputs.put(getLinkedInJobs0ipn0, getLinkedInJobs0ipv0);
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("serviceWSDL", (String) getLinkedInJobs0wsdl);
			hashMap.put("operationName", (String) getLinkedInJobs0operation);
			hashMap.put("operationInputs", (HashMap<String, String>) getLinkedInJobs0operationInputs);
			hashMap.put("backupFile", (String) "");
			StartWSInvocatorEvent startWS = new StartWSInvocatorEvent(hashMap);
			this.fireStartWSInvocatorEvent(startWS, this.mainAci, null);
			
		} else if(mainControlFlow == 4){
			//Can't evaluate AND-split control flow parameters because both flows get here with the same value
			//so we are evaluating the activity received and comparing it with the parallel activities.
			if(aci.getActivity().equals(this.andSplit1Aci.getActivity())){
				System.out.println("EndGetData Event received on execution step: "+mainControlFlow);
				System.out.println("Parallel Split 1 execution step: "+branchControlFlow1);
				//Setting AND-split1 Activity object to current state
				this.andSplit1Aci = aci;
				//Getting DataAccess Service response data on step AND-split1 2
				GetDataTelcoService1op0 = event.getValue();
				
				//Invoking SendEmail Service on step AND-split1 2
				branchControlFlow1 = 2;
				//Parameter mapping
				SendEmailService0ip0 = GetDataTelcoService1op0;
				SendEmailService0ip1 = getLinkedInJobs0opv0.get(0); //Only getting the first element of the array
				HashMap<String, Object> operationInputs = new HashMap<String, Object>();
				operationInputs.put("email", (String) SendEmailService0ip0);
				operationInputs.put("message", (String) SendEmailService0ip1);
				operationInputs.put("subject", SendEmailService0ip2);
				StartSendEmailTelcoServiceEvent sendEmailEvent = new StartSendEmailTelcoServiceEvent(operationInputs);
				this.fireStartSendEmailTelcoServiceEvent(sendEmailEvent, this.andSplit1Aci, null);
			}
			
			if(aci.getActivity().equals(this.andSplit2Aci.getActivity())){
				System.out.println("EndGetData Event received on execution step: "+mainControlFlow);
				System.out.println("Parallel Split 2 execution step: "+branchControlFlow2);
				//Setting AND-split2 Activity object to current state
				this.andSplit2Aci = aci;
				//Getting DataAccess Service response data on step AND-split2 2
				GetDataTelcoService2op0 = event.getValue();
				
				//Invoking Twitter WS on step AND-split2 2
				branchControlFlow2 = 2;
				//Parameter mapping
				sendTwitterMessage0ipv0 = GetDataTelcoService2op0;
				sendTwitterMessage0ipv1 = getLinkedInJobs0opv0.get(0); //Only getting the first element of the array
				sendTwitterMessage0operationInputs.put(sendTwitterMessage0ipn0, sendTwitterMessage0ipv0);
				sendTwitterMessage0operationInputs.put(sendTwitterMessage0ipn1, sendTwitterMessage0ipv1);
				
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("serviceWSDL", (String) sendTwitterMessage0wsdl);
				hashMap.put("operationName", (String) sendTwitterMessage0operation);
				hashMap.put("operationInputs", (HashMap<String, String>) sendTwitterMessage0operationInputs);
				hashMap.put("backupFile", (String) "");
				StartWSInvocatorEvent wsEvent = new StartWSInvocatorEvent(hashMap);
				this.fireStartWSInvocatorEvent(wsEvent, this.andSplit2Aci, null);
			}  
			
		} else if(mainControlFlow == 5){
			System.out.println("EndGetData Event received on execution step: "+mainControlFlow);
			//Setting main Activity object to current state
			this.mainAci = aci;
			//Getting DataAccess Service response data on step 6
			GetDataTelcoService3op0 = event.getValue();
			
			//Invoking MediaCall Service on step 6
			mainControlFlow = 6;
			//Parameter mapping
			MediaCallTelcoService0ip0 = GetDataTelcoService3op0;
			MediaCallTelcoService0ip1 = getLinkedInJobs0opv0.get(0); //Only getting the first element of the array
			HashMap<String, Object> operationInputs = new HashMap<String, Object>();
			operationInputs.put("uriSip", (String) MediaCallTelcoService0ip0);
			operationInputs.put("text", (String) MediaCallTelcoService0ip1);
			StartMediaCallTelcoServiceEvent mediaCallEvent = new StartMediaCallTelcoServiceEvent(operationInputs);
			this.fireStartMediaCallTelcoServiceEvent(mediaCallEvent, this.mainAci, null);
			
		} else {
			aci.detach(this.sbbContext.getSbbLocalObject());
		}
	}
	
	public void onEndWSInvocatorEvent(EndWSInvocatorEvent event, ActivityContextInterface aci){
		if(mainControlFlow == 2){
			System.out.println("EndWebService Event received on execution step: "+mainControlFlow);
			//Setting mainAci object to current state
			this.mainAci = aci;
			//Getting Web Service response Data on step 3
			mainControlFlow = 3;
			getLinkedInJobs0opv0 = event.getOperationOutputs().get(getLinkedInJobs0opn0);
			
			//AND-split control element
			mainControlFlow = 4;
			//Setting AND-split Activities for parallel execution
			this.andSplit1Aci = this.createNullActivityACI();
			this.andSplit1Aci.attach(this.sbbContext.getSbbLocalObject());
			this.andSplit2Aci = this.createNullActivityACI();
			this.andSplit2Aci.attach(this.sbbContext.getSbbLocalObject());
			
			//Invoking DataAccess Service on execution step AND-split1 1 
			branchControlFlow1 = 1;
			//Parameter mapping
			GetDataTelcoService1ip0 = startpv0;
			HashMap<String, Object> operationInputs1 = new HashMap<String, Object>();
			operationInputs1.put("parameter", (String) GetDataTelcoService1ip1);
			operationInputs1.put("identification", (String) GetDataTelcoService1ip0);
			StartGetDataTelcoServiceEvent dataAccessEvent1 = new StartGetDataTelcoServiceEvent(operationInputs1);
			this.fireStartGetDataTelcoServiceEvent(dataAccessEvent1, this.andSplit1Aci, null);
			
			//Invoking DataAccess Service on execution step AND-split2 1
			branchControlFlow2 = 1;
			//Parameter mapping
			GetDataTelcoService2ip0 = startpv0;
			HashMap<String, Object> operationInputs2 = new HashMap<String, Object>();
			operationInputs2.put("parameter", (String) GetDataTelcoService2ip1);
			operationInputs2.put("identification", (String) GetDataTelcoService2ip0);
			StartGetDataTelcoServiceEvent dataAccessEvent2 = new StartGetDataTelcoServiceEvent(operationInputs2);
			this.fireStartGetDataTelcoServiceEvent(dataAccessEvent2, this.andSplit2Aci, null);
			
		} else if(mainControlFlow == 4){
			if(branchControlFlow2 == 2){
				System.out.println("EndWebService Event received on execution step: "+mainControlFlow);
				System.out.println("Parallel Split 2 execution step: "+branchControlFlow2);
				//Setting AND-split2 Activity object to current state
				this.andSplit2Aci = aci;
				//Getting Web Service Response data on step AND-split 3
				branchControlFlow2 = 3;
				sendTwitterMessage0opv0 = event.getOperationOutputs().get(sendTwitterMessage0opn0).get(0);
				
				//Detaching parallel activity object
				this.andSplit2Aci.detach(this.sbbContext.getSbbLocalObject());
				
				//AND-join control element condition
				if(branchControlFlow1 == 3){
					//Invoking DataAccess Service on execution step 5
					mainControlFlow = 5;
					//Finalizing control flow branches
					branchControlFlow1 = 0;
					branchControlFlow2 = 0;
					//Parameter mapping
					GetDataTelcoService3ip0 = startpv0;
					HashMap<String, Object> operationInputs = new HashMap<String, Object>();
					operationInputs.put("parameter", (String) GetDataTelcoService3ip1);
					operationInputs.put("identification", (String) GetDataTelcoService3ip0);
					StartGetDataTelcoServiceEvent dataAccessEvent = new StartGetDataTelcoServiceEvent(operationInputs);
					this.fireStartGetDataTelcoServiceEvent(dataAccessEvent, this.mainAci, null);
				}
			}
		} else {
			aci.detach(this.sbbContext.getSbbLocalObject());
		}
	}
	
	
	public void onEndSendEmailTelcoServiceEvent(EndSendEmailTelcoServiceEvent event, ActivityContextInterface aci){
		if(mainControlFlow == 4){
			if(branchControlFlow1 == 2){
				System.out.println("EndSendEmail Event received on execution step: "+mainControlFlow);
				System.out.println("Parallel Split 1 execution step: "+branchControlFlow1);
				//Setting AND-split1 Activity object to current state
				this.andSplit1Aci = aci;
				//Getting SendEmail Service response on step AND-split1 3
				branchControlFlow1 = 3;
				SendEmailService0op0 = event.getSended();
				
				//Detaching parallel activity object
				this.andSplit1Aci.detach(this.sbbContext.getSbbLocalObject());
				
				//AND-join control element condition
				if(branchControlFlow2 == 3){
					//Invoking DataAccess Service on execution step 5
					mainControlFlow = 5;
					//Finalizing control flow branches
					branchControlFlow1 = 0;
					branchControlFlow2 = 0;
					//Parameter mapping
					GetDataTelcoService3ip0 = startpv0;
					HashMap<String, Object> operationInputs = new HashMap<String, Object>();
					operationInputs.put("parameter", (String) GetDataTelcoService3ip1);
					operationInputs.put("identification", (String) GetDataTelcoService3ip0);
					StartGetDataTelcoServiceEvent dataAccessEvent = new StartGetDataTelcoServiceEvent(operationInputs);
					this.fireStartGetDataTelcoServiceEvent(dataAccessEvent, this.mainAci, null);
				}
			}
		}
	}
	
	public void onEndMediaCallTelcoServiceEvent(EndMediaCallTelcoServiceEvent event, ActivityContextInterface aci){
		if(mainControlFlow == 6){
			System.out.println("End MediaCall Event received on execution step: "+mainControlFlow);
			//Setting main Activity object to current state
			this.mainAci = aci;
			//Getting MediaCall Service response on step 7
			MediaCallTelcoService0op0 = event.getCommited();
			mainControlFlow = 7;
			System.out.println("LinkedIn Job Notificator Service execution completed.");
			
			PrintWriter w = null;
			try {
				w = httpResponse.getWriter();
				w.print("<html><body><center><h1>LinkedIn Job Notificator Service execution completed!!</h1></center></body></html>");
				w.flush();
				httpResponse.flushBuffer();
				httpAci.detach(this.sbbContext.getSbbLocalObject());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			this.getEventContext().resumeDelivery();
			
			//Service execution completed
			this.mainAci.detach(this.sbbContext.getSbbLocalObject());
		}
	}
	
	private ActivityContextInterface createNullActivityACI(){
		NullActivity nullActivity = this.nullActivityFactory.createNullActivity();
		ActivityContextInterface nullActivityACI = null;
		nullActivityACI = this.nullACIFactory.getActivityContextInterface(nullActivity);
		return nullActivityACI;
	}
	
	
	public abstract void setEventContext(EventContext context);
	public abstract EventContext getEventContext();
	
	// TODO: Perform further operations if required in these methods.
	public void setSbbContext(SbbContext context) { 
		this.sbbContext = context;
		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");
			nullActivityFactory = (NullActivityFactory) ctx.lookup("slee/nullactivity/factory");
			nullACIFactory = (NullActivityContextInterfaceFactory) ctx.lookup("slee/nullactivity/activitycontextinterfacefactory");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
    public void unsetSbbContext() { this.sbbContext = null; }
    
    // TODO: Implement the lifecycle methods if required
    public void sbbCreate() throws javax.slee.CreateException {}
    public void sbbPostCreate() throws javax.slee.CreateException {}
    public void sbbActivate() {}
    public void sbbPassivate() {}
    public void sbbRemove() {}
    public void sbbLoad() {}
    public void sbbStore() {}
    public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {}
    public void sbbRolledBack(RolledBackContext context) {}
    
    public abstract void fireStartWSInvocatorEvent (StartWSInvocatorEvent event, ActivityContextInterface aci, Address address);
    public abstract void fireStartGetDataTelcoServiceEvent (StartGetDataTelcoServiceEvent event, ActivityContextInterface aci, Address address);
    public abstract void fireStartSendEmailTelcoServiceEvent (StartSendEmailTelcoServiceEvent event, ActivityContextInterface aci, Address address);
    public abstract void fireStartMediaCallTelcoServiceEvent (StartMediaCallTelcoServiceEvent event, ActivityContextInterface aci, Address address);

	
	/**
	 * Convenience method to retrieve the SbbContext object stored in setSbbContext.
	 * 
	 * TODO: If your SBB doesn't require the SbbContext object you may remove this 
	 * method, the sbbContext variable and the variable assignment in setSbbContext().
	 *
	 * @return this SBB's SbbContext object
	 */
	
	protected SbbContext getSbbContext() {
		return sbbContext;
	}

	private SbbContext sbbContext; // This SBB's SbbContext

}
