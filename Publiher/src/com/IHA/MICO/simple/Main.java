// ****************************************************************************
//         (c) Copyright, Real-Time Innovations, All rights reserved.       
//                                                                          
//         Permission to modify and use for internal purposes granted.      
// This software is provided "as is", without warranty, express or implied. 
//                                                                          
// ****************************************************************************

package com.IHA.MICO.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.topic.Topic;
import com.rti.dds.type.builtin.StringDataWriter;
import com.rti.dds.type.builtin.StringTypeSupport;

//****************************************************************************
public class Main {
    public static final void main(String[] args) {
        // Create the DDS Domain participant on domain ID 0
        DomainParticipant participant = DomainParticipantFactory.get_instance().create_participant(
                0, // Domain ID = 0
                DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, 
                null, // listener
                StatusKind.STATUS_MASK_NONE);
        if (participant == null) {
            System.err.println("Unable to create domain participant");
            return;
        }

        // Create the topics
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

        // Create the data writers using the default publisher
        StringDataWriter dataWriterNews =
            (StringDataWriter) participant.create_datawriter(
                topicNews,
                Publisher.DATAWRITER_QOS_DEFAULT,
                null, // listener
                StatusKind.STATUS_MASK_NONE);
        if (dataWriterNews == null) {
            System.err.println("Unable to create data writer\n");
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

        System.out.println("Ready to write data.");
        System.out.println("When the subscriber is ready, you can start writing.");
        System.out.print("Press CTRL+C to terminate or enter an empty line to do a clean shutdown.\n\n");

        for (;;)
        {
        dataWriterNews.write("News!", InstanceHandle_t.HANDLE_NIL);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        dataWriterStocks.write("Stocks!", InstanceHandle_t.HANDLE_NIL);
        }

        /*BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                System.out.print("Please type a message> ");
                String toWrite = reader.readLine();
                if (toWrite == null) break;     // shouldn't happen
                dataWriterNews.write(toWrite, InstanceHandle_t.HANDLE_NIL);
                if (toWrite.equals("")) break;
            }
        } catch (IOException e) {
            // This exception can be thrown from the BufferedReader class
            e.printStackTrace();
        } catch (RETCODE_ERROR e) {
            // This exception can be thrown from DDS write operation
            e.printStackTrace();
        }

        System.out.println("Exiting...");
        participant.delete_contained_entities();
        DomainParticipantFactory.get_instance().delete_participant(participant);
        */
    }
}
