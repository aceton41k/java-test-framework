package template.framework.test.api.model.response;

import lombok.Getter;

/**
 * Class represents cat's fact response body
 */
@Getter
public class CatFactResponse {
    public String fact;
    public Integer length;
}
