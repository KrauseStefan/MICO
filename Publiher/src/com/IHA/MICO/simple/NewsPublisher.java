package com.IHA.MICO.simple;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.infrastructure.*;
import com.rti.dds.publication.DataWriterAdapter;
import com.rti.dds.publication.DataWriterQos;
import com.rti.dds.publication.Publisher;
import com.rti.dds.topic.Topic;
import com.rti.dds.topic.TopicQos;
import com.rti.dds.type.builtin.StringDataWriter;
import com.rti.dds.type.builtin.StringTypeSupport;

/**
 * Created with IntelliJ IDEA.
 * User: t
 * Date: 11/17/13
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */

public class NewsPublisher extends DataWriterAdapter{
    private boolean shutdown_flag = true;

    public NewsPublisher(DomainParticipant participant) {
        Topic topicNews = participant.create_topic(
                "News",
                StringTypeSupport.get_type_name(),
                DomainParticipant.TOPIC_QOS_DEFAULT,
                null, // listener
                StatusKind.STATUS_MASK_NONE);
        if (topicNews == null) {
            System.err.println("Unable to create topic.");
            return;
        }

        DataWriterQos dwQos = new DataWriterQos();
        dwQos = Publisher.DATAWRITER_QOS_DEFAULT;
        dwQos.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;

        StringDataWriter dataWriterNews =
                (StringDataWriter) participant.create_datawriter(
                        topicNews,
                        dwQos,
                        null, // listener
                        StatusKind.STATUS_MASK_NONE);
        if (dataWriterNews == null) {
            System.err.println("Unable to create data writer\n");
            return;
        }

        String[] myStringArray = {"Federal agents raids gunshop, find weapons!","Enraged Cow Injures Farmer With Ax","Man Struck By Lightning Faces Battery Charge"};

        for (int i=0; i<10; i++)
        {
            dataWriterNews.write(myStringArray[i%3], InstanceHandle_t.HANDLE_NIL);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public boolean getShutdownFlag(){
        return shutdown_flag;
    }
}
