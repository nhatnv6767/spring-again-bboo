package com.ra.repository.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCriteriaQueryConsumer implements Consumer<SearchCriteria> {
    @Override
    public void accept(SearchCriteria searchCriteria) {

    }
}
