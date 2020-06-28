package com.kropotov.asrd.dto.items;

import com.kropotov.asrd.dto.SimpleUser;
import com.kropotov.asrd.entities.docs.File;
import com.kropotov.asrd.entities.enums.Location;
import com.kropotov.asrd.entities.titles.DeviceTitle;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DeviceDto {
    private Long id;
    private DeviceTitle deviceTitle;
    private String number;
    private Location location;
    private String purpose;
    private String purposePassport;
    private String vintage;
    private int vpNumber;
    private String otkDate;
    private String vpDate;
    private SimpleControlSystem system;
    private List<SimpleDeviceComponent> components = new ArrayList<>();
    private SimpleUser user;
    private List<File> files;
//    private List<Long> invoicesId;
//    private List<Long> actsId;

    public void addComponent(SimpleDeviceComponent simpleDeviceComponent) {
        if (components == null) {
            components = new ArrayList<>();
        }
        components.add(simpleDeviceComponent);
    }

    public boolean addFile(@NonNull File file) {
        if (files == null) {
            files = new ArrayList<>();
        }
        return files.add(file);
    }
}
