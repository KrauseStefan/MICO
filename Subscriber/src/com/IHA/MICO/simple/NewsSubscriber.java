package com.IHA.MICO.simple;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.topic.Topic;
import com.rti.dds.type.builtin.StringDataReader;
import com.rti.dds.type.builtin.StringTypeSupport;

public class NewsSubscriber extends DataReaderAdapter {

    // For clean shutdown sequence
    private boolean shutdown_flag = true;

    public NewsSubscriber(DomainParticipant participant){

        Topic topic = participant.create_topic(
                "News",
                StringTypeSupport.get_type_name(),
                DomainParticipant.TOPIC_QOS_DEFAULT,
                null, // listener
                StatusKind.STATUS_MASK_NONE);
        if (topic == null) {
            System.err.println("Unable to create topic.");
            return;
        }

        // Create the data reader using the default publisher
        StringDataReader dataReader =
                (StringDataReader) participant.create_datareader(
                        topic,
                        Subscriber.DATAREADER_QOS_DEFAULT,
                        this, // Listener
                        StatusKind.DATA_AVAILABLE_STATUS);

        if (dataReader == null) {
            System.err.println("Unable to create DDS Data Reader");
            return;
        }
    }


    /*
 * This method gets called back by DDS when one or more data samples have
 * been received.
 */
    public void on_data_available(DataReader reader) {
        StringDataReader stringReader = (StringDataReader) reader;
        SampleInfo info = new SampleInfo();
        for (; ; ) {
            try {
                String sample = stringReader.take_next_sample(info);
                if (info.valid_data) {
                    System.out.println("Braking news: " + sample);
                    if (sample.length() <= 0) {
                        shutdown_flag = true;
                    }
                }
            } catch (RETCODE_NO_DATA noData) {
                // No more data to read
                break;
            } catch (RETCODE_ERROR e) {
                // An error occurred
                e.printStackTrace();
            }
        }
    }

    boolean getShutdownFlag() {
        return shutdown_flag;
    }

}
