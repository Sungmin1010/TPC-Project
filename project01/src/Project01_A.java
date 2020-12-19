import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kr.blue.BookDTO;

import java.util.ArrayList;
import java.util.List;

public class Project01_A {
    public static void main(String[] args) {
        //Object -> jSON(String)
        BookDTO dto = new BookDTO("자바", 21000, "에이콘", 670);
        Gson g = new Gson();
        String json = g.toJson(dto);
        System.out.println(json);

        //JSON(String) -> Object(BookDTO)
        BookDTO dto1 = g.fromJson(json, BookDTO.class);
        System.out.println(dto1);

        //여러개의 데이터
        List<BookDTO> lst = new ArrayList<>();
        lst.add(new BookDTO("자바1", 21000, "에이콘1", 670));
        lst.add(new BookDTO("자바2", 51000, "에이콘2", 770));
        lst.add(new BookDTO("자바3", 61000, "에이콘3", 870));
        String lstJson = g.toJson(lst);
        System.out.println(lstJson);

        //JSON(String) -> Object(list<BookDTO>)
        List<BookDTO> list1 = g.fromJson(lstJson, new TypeToken<List<BookDTO>>(){}.getType());
        for(BookDTO vo : list1){
            System.out.println(vo);
        }

        //GSON을 사용하기 위해서는 객체타입을 미리 만들어 둬야한다(BookDTO)
        //객체를 만들지 않고 사용하는 방법은 없나?
    }
}
