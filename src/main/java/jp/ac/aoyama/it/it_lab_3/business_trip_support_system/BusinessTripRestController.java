package jp.ac.aoyama.it.it_lab_3.business_trip_support_system;

//15818015_InoueYoshiaki
import jp.ac.aoyama.it.it_lab_3.business_trip2.BusinessTripModel;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

//error

@RestController
public class BusinessTripRestController {

    private static final String CONTAINER_ID = "business_trip_final3_1.0.0";
    //business_trip2をカスタムしたやつ
    private static final String KIE_SERVER_URL = "http://localhost:8080/kie-server/services/rest/server";
    private static final String KIE_SERVER_USER = "admin";
    private static final String KIE_SERVER_PASSWORD = "AoyamaIT";
    
    public BusinessTripModel insertAndExecute(BusinessTripModel btm, KieCommands commandFactory, RuleServicesClient ruleClient) {
        Command<?> insert = commandFactory.newInsert(btm, "btm");
        Command<?> fireAllRules = commandFactory.newFireAllRules();
        Command<?> batchCommand = commandFactory.newBatchExecution(Arrays.asList(insert, fireAllRules));
        ServiceResponse<ExecutionResults> response = ruleClient.executeCommandsWithResults(CONTAINER_ID, batchCommand);
        ExecutionResults results = response.getResult();
        return (BusinessTripModel) results.getValue("btm");
    }

    public void dispose(KieCommands commandFactory, RuleServicesClient ruleClient) {
        Command<?> dispose = commandFactory.newDispose();
        Command<?> batchCommand = commandFactory.newBatchExecution(Arrays.asList(dispose));
        ruleClient.executeCommandsWithResults(CONTAINER_ID, batchCommand);
    }

    @PostMapping("/calc_All_final")
    public BusinessTripModel calcAll(@RequestBody BusinessTripModel model) {
        System.out.println(model.getName());
        System.out.println(model.getNumberOfNights());
        System.out.println(model.getAccommodationFee());

        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(KIE_SERVER_URL,
                KIE_SERVER_USER,
                KIE_SERVER_PASSWORD);
        config.setMarshallingFormat(MarshallingFormat.JSON);

        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);
        RuleServicesClient ruleClient = client.getServicesClient(RuleServicesClient.class);
        KieCommands commandFactory = KieServices.Factory.get().getCommands();

        //modelにruleクライアントで計算した結果を保存
        model = insertAndExecute(model, commandFactory, ruleClient);
        dispose(commandFactory, ruleClient);

        Output.createExcelFile(model);
        return model;
    }

}
