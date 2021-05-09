package io.github.matwein.xmc.be.common.factories;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import io.github.matwein.xmc.be.entities.BinaryData;

@Component
public class BinaryDataFactory {
    public BinaryData create(byte[] content, String description) {
        if (content == null) {
            return null;
        }

        BinaryData binaryData = new BinaryData();

        binaryData.setRawData(content);
        binaryData.setDescription(description);
        binaryData.setHash(DigestUtils.md5DigestAsHex(content));
        binaryData.setSize(content.length);

        return binaryData;
    }
}
