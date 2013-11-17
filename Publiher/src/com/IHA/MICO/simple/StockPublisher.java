package com.IHA.MICO.simple;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.DataWriterAdapter;
import com.rti.dds.publication.Publisher;
import com.rti.dds.topic.Topic;
import com.rti.dds.type.builtin.StringDataWriter;
import com.rti.dds.type.builtin.StringTypeSupport;

/**
 * Created with IntelliJ IDEA.
 * User: t
 * Date: 11/17/13
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class StockPublisher extends DataWriterAdapter {
    private boolean shutdown_flag = false;

    public StockPublisher(DomainParticipant participant){
        Topic topicStocks = participant.create_topic(
                "Stocks",
                StringTypeSupport.get_type_name(),
                DomainParticipant.TOPIC_QOS_DEFAULT,
                null, // listener
                StatusKind.STATUS_MASK_NONE);
        if (topicStocks == null) {
            System.err.println("Unable to create topic.");
            return;
        }

        StringDataWriter dataWriterStocks =
                (StringDataWriter) participant.create_datawriter(
                        topicStocks,
                        Publisher.DATAWRITER_QOS_DEFAULT,
                        null, // listener
                        StatusKind.STATUS_MASK_NONE);
        if (dataWriterStocks == null) {
            System.err.println("Unable to create data writer\n");
            return;
        }

        String[] myStringArray = {"Google 245","Microsoft 110","Apple 500"};

        for (int i=0; i<10;i++)
        {
            dataWriterStocks.write(myStringArray[i%3], InstanceHandle_t.HANDLE_NIL);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        dataWriterStocks.write("", InstanceHandle_t.HANDLE_NIL);
        shutdown_flag = true;
    }

    public boolean getShutdownFlag(){
        return shutdown_flag;
    }
}
