package template.framework.test.model.ws;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Класс представляет сообщение websocket
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    public String text;
}
