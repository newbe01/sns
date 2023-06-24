package com.example.sns.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmArgs {

    private Integer fromUserId; // 알람을 발생시킨 사람
    private Integer targetId;

}
