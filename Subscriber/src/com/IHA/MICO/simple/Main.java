package com.IHA.MICO.simple;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.topic.Topic;
import com.rti.dds.type.builtin.StringDataReader;
import com.rti.dds.type.builtin.StringTypeSupport;

public class Main {

    private static NewsSubscriber newsSubscriber;
    private static StocksSubscriber stocksSubscriber;

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

        newsSubscriber = new NewsSubscriber(participant);
        stocksSubscriber  = new StocksSubscriber(participant);

        System.out.println("Ready to read data.");
        System.out.println("Press CTRL+C to terminate.");
        for (; ; ) {
            try {
                Thread.sleep(2000);
                if (newsSubscriber.getShutdownFlag() && stocksSubscriber.getShutdownFlag())
                    break;
            } catch (InterruptedException e) {
                // Nothing to do...
            }
        }

        System.out.println("Shutting down...");
        participant.delete_contained_entities();
        DomainParticipantFactory.get_instance().delete_participant(participant);
    }

}
