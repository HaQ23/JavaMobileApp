-- Dodawanie quizu
INSERT INTO quiz (name, time) VALUES ('Zaliczenie z Javy', 60);
INSERT INTO quiz (name, time) VALUES ('Java - Podstawy Języka', 15);

-- Pobranie ID dodanego quizu
-- Zakładamy, że id quizu to 1
-- Można dostosować poniższe ID zgodnie z faktycznym ID z bazy danych po dodaniu

-- Dodawanie pytań do quizu
INSERT INTO question (name, quiz_id) VALUES ('W wyniku kompilacji kodu źródłowego Java otrzymano między innymi pliki Class$1.class, Class$2.class. Dlaczego w nazwach plików klasowych po znaku $ są liczby 1 i 2:', 1);
INSERT INTO question (name, quiz_id) VALUES ('W języku Java rozkład komponentów (przycisków) dla biblioteki Swing w oknie aplikacji jak na rysunku niżej realizuje klasa:', 1);
INSERT INTO question (name, quiz_id) VALUES ('Wykonanie poniższego programu spowoduje wyświetlenie:', 1);
INSERT INTO question (name, quiz_id) VALUES ('W języku Java w klasie serwletów javax.servlet.http.HttpServlet za obsługę żądań typu GET odpowiada metoda:', 1);
INSERT INTO question (name, quiz_id) VALUES ('Wykonanie poniższego kodu języka Java powoduje wyświetlenie na konsoli:', 1);
INSERT INTO question (name, quiz_id) VALUES ('Która składnia dostępu do kolekcji ArrayList<String> myList = new ArrayList<String>(); za pomocą pętli for jest poprawna:', 1);

INSERT INTO question (name, quiz_id) VALUES ('Co to jest deklaracja zmiennej w języku Java?', 2);
INSERT INTO question (name, quiz_id) VALUES ('Jakie są podstawowe typy danych w języku Java?', 2);
INSERT INTO question (name, quiz_id) VALUES ('Czym różnią się zmienne typu int i Integer w języku Java?', 2);
INSERT INTO question (name, quiz_id) VALUES ('Co to jest instrukcja warunkowa "if" w języku Java i jak się jej używa?', 2);
INSERT INTO question (name, quiz_id) VALUES ('Jakie są różnice między pętlą "for" a pętlą "while" w języku Java?', 2);
INSERT INTO question (name, quiz_id) VALUES ('Co to jest tablica (array) w języku Java i jak się jej używa?', 2);
INSERT INTO question (name, quiz_id) VALUES ('Jakie są różnice między klasą a obiektem w języku Java?', 2);
INSERT INTO question (name, quiz_id) VALUES ('Jakie są podstawowe zasady tworzenia metod (funkcji) w języku Java?', 2);
INSERT INTO question (name, quiz_id) VALUES ('Co to jest dziedziczenie (inheritance) w języku Java i jakie korzyści ono niesie?', 2);
INSERT INTO question (name, quiz_id) VALUES ('Jak obsługiwać wyjątki (exceptions) w języku Java?', 2);
INSERT INTO question (name, quiz_id) VALUES ('Co oznacza poniższy kod?', 2);
INSERT INTO question (name, quiz_id) VALUES ('Co spowoduje wywołanie poniższej metody?', 2);


-- Pobranie ID dodanych pytań
-- Zakładamy, że id pytań to kolejno od 1 do 6
-- Można dostosować poniższe ID zgodnie z faktycznym ID z bazy danych po dodaniu

-- Dodawanie odpowiedzi do pytań
-- Pytanie 1
INSERT INTO answer (name, correct, question_id) VALUES ('Ponieważ klasa zawierała dwie klasy wewnętrzne o nazwach 1 i 2', true, 1);
INSERT INTO answer (name, correct, question_id) VALUES ('Ponieważ klasa zawierała dwie anonimowe klasy wewnętrzne', false, 1);
INSERT INTO answer (name, correct, question_id) VALUES ('Ponieważ klasa zawierała dwie nazwy klasy abstrakcyjne', false, 1);
INSERT INTO answer (name, correct, question_id) VALUES ('Ponieważ klasa zawierała dwie nazwane klasy adaptacyjne', false, 1);

-- Pytanie 2
INSERT INTO answer (name, correct, question_id) VALUES ('FlowLayout', false, 2);
INSERT INTO answer (name, correct, question_id) VALUES ('GridLayout', false, 2);
INSERT INTO answer (name, correct, question_id) VALUES ('BorderLayout', false, 2);
INSERT INTO answer (name, correct, question_id) VALUES ('BoxLayout', true, 2);

-- Pytanie 3
INSERT INTO answer (name, correct, question_id) VALUES ('List:1List:2List:3', true, 3);
INSERT INTO answer (name, correct, question_id) VALUES ('List:4List:5List:6', false, 3);
INSERT INTO answer (name, correct, question_id) VALUES ('List:123456', false, 3);
INSERT INTO answer (name, correct, question_id) VALUES ('error', false, 3);
INSERT INTO answer (name, correct, question_id) VALUES ('List:(123456)', false, 3);

-- Pytanie 4
INSERT INTO answer (name, correct, question_id) VALUES ('processRequest()', false, 4);
INSERT INTO answer (name, correct, question_id) VALUES ('doGet()', true, 4);
INSERT INTO answer (name, correct, question_id) VALUES ('doPost()', false, 4);
INSERT INTO answer (name, correct, question_id) VALUES ('Język Java nie obsługuje żądań typu GET', false, 4);

-- Pytanie 5
INSERT INTO answer (name, correct, question_id) VALUES ('AC', true, 5);
INSERT INTO answer (name, correct, question_id) VALUES ('BC', false, 5);
INSERT INTO answer (name, correct, question_id) VALUES ('BD', false, 5);
INSERT INTO answer (name, correct, question_id) VALUES ('AD', false, 5);

-- Pytanie 6
INSERT INTO answer (name, correct, question_id) VALUES ('for (int i = 0; i < myList.size(); i++) { System.out.println(myList(i)); }', false, 6);
INSERT INTO answer (name, correct, question_id) VALUES ('for (String s : myList) { System.out.println(s); }', true, 6);
INSERT INTO answer (name, correct, question_id) VALUES ('for (int s : myList) { System.out.println(s); }', false, 6);
INSERT INTO answer (name, correct, question_id) VALUES ('for (int s = 0) { System.out.println(s); }', false, 6);

-- Pytanie 7
INSERT INTO answer (name, correct, question_id) VALUES ('Deklaracja zmiennej to sposób zarezerwowania miejsca w pamięci i określenia typu danych, której wartość może być przypisana.', true, 7);
INSERT INTO answer (name, correct, question_id) VALUES ('Deklaracja zmiennej to operacja wykonywana tylko w metodach', false, 7);
INSERT INTO answer (name, correct, question_id) VALUES ('Deklaracja zmiennej to instrukcja warunkowa', false, 7);
INSERT INTO answer (name, correct, question_id) VALUES ('Deklaracja zmiennej to zdefiniowanie metody', false, 7);

-- Pytanie 8
INSERT INTO answer (name, correct, question_id) VALUES ('byte, short, int, long, float, double, char, boolean', true,8);
INSERT INTO answer (name, correct, question_id) VALUES ('string, double, char, boolean, int', false, 8);
INSERT INTO answer (name, correct, question_id) VALUES ('int, float, boolean, double', false, 8);
INSERT INTO answer (name, correct, question_id) VALUES ('float, double, boolean, char', false, 8);

-- Pytanie 9
INSERT INTO answer (name, correct, question_id) VALUES ('Typ int jest typem prymitywnym, podczas gdy Integer jest klasą opakowującą (wrapper class).', true, 9);
INSERT INTO answer (name, correct, question_id) VALUES ('Typ int jest typem klasowym, podczas gdy Integer jest typem prymitywnym.', false, 9);
INSERT INTO answer (name, correct, question_id) VALUES ('Typ int i Integer są używane zamiennie w języku Java.', false, 9);
INSERT INTO answer (name, correct, question_id) VALUES ('Typ int i Integer są niezależnymi typami w języku Java i nie mają różnic.', false, 9);

-- Pytanie 10
INSERT INTO answer (name, correct, question_id) VALUES ('Instrukcja warunkowa "if" w języku Java służy do wykonania określonych instrukcji tylko wtedy, gdy podany warunek jest prawdziwy.', true, 10);
INSERT INTO answer (name, correct, question_id) VALUES ('Instrukcja warunkowa "if" w języku Java jest używana tylko do pętli', false, 10);
INSERT INTO answer (name, correct, question_id) VALUES ('Instrukcja warunkowa "if" w języku Java służy do wykonania określonych instrukcji tylko wtedy, gdy podany warunek jest fałszywy.', false, 10);
INSERT INTO answer (name, correct, question_id) VALUES ('Instrukcja warunkowa "if" w języku Java jest używana tylko do pętli', false, 10);

-- Pytanie 11
INSERT INTO answer (name, correct, question_id) VALUES ('Pętla "for" jest używana, gdy liczba iteracji jest znana, natomiast pętla "while" jest używana, gdy liczba iteracji nie jest znana z góry.', true, 11);
INSERT INTO answer (name, correct, question_id) VALUES ('Pętla "for" nie może być używana w Java', false, 11);
INSERT INTO answer (name, correct, question_id) VALUES ('Pętla "while" nie może być używana w Java', false, 11);
INSERT INTO answer (name, correct, question_id) VALUES ('Pętla "for" i "while" mają identyczne zastosowanie w Java', false, 11);

-- Pytanie 12
INSERT INTO answer (name, correct, question_id) VALUES ('Tablica (array) w języku Java jest to struktura danych służąca do przechowywania wielu wartości tego samego typu, które są indeksowane i mogą być dostępne za pomocą pojedynczego identyfikatora.', true, 12);
INSERT INTO answer (name, correct, question_id) VALUES ('Tablica (array) w języku Java służy tylko do przechowywania jednej wartości', false, 12);
INSERT INTO answer (name, correct, question_id) VALUES ('Tablica (array) w języku Java jest używana do przechowywania tylko wartości typu int', false, 12);
INSERT INTO answer (name, correct, question_id) VALUES ('Tablica (array) w języku Java służy do przechowywania wartości typu String', false, 12);

-- Pytanie 13
INSERT INTO answer (name, correct, question_id) VALUES ('Klasa jest szablonem, na podstawie którego tworzone są obiekty, natomiast obiekt jest egzemplarzem klasy, posiadającym swoje własne stany i zachowania.', true, 13);
INSERT INTO answer (name, correct, question_id) VALUES ('Klasa i obiekt są synonimami w języku Java.', false, 13);
INSERT INTO answer (name, correct, question_id) VALUES ('Klasa i obiekt są niezależnymi konceptami i nie mają wspólnych cech.', false, 13);
INSERT INTO answer (name, correct, question_id) VALUES ('Obiekt jest szablonem, na podstawie którego tworzone są klasy, natomiast klasa jest egzemplarzem obiektu.', false, 13);

-- Pytanie 14
INSERT INTO answer (name, correct, question_id) VALUES ('Podstawowe zasady tworzenia metod w języku Java obejmują określenie typu zwracanego metody, nazwy metody i lista parametrów (jeśli występują).', true, 14);
INSERT INTO answer (name, correct, question_id) VALUES ('Metody w języku Java mogą nie mieć określonego typu zwracanego.', false, 14);
INSERT INTO answer (name, correct, question_id) VALUES ('Nazwa metody nie jest istotna w języku Java.', false, 14);
INSERT INTO answer (name, correct, question_id) VALUES ('Parametry są opcjonalne w języku Java.', false, 14);

-- Pytanie 15
INSERT INTO answer (name, correct, question_id) VALUES ('Dziedziczenie (inheritance) w języku Java umożliwia tworzenie nowych klas na podstawie istniejących klas, co pozwala na ponowne wykorzystanie kodu, rozszerzenie funkcjonalności i organizację kodu w hierarchię.', true, 15);
INSERT INTO answer (name, correct, question_id) VALUES ('Dziedziczenie (inheritance) w języku Java jest używane tylko do implementowania interfejsów.', false, 15);
INSERT INTO answer (name, correct, question_id) VALUES ('Dziedziczenie (inheritance) w języku Java nie jest możliwe.', false, 15);
INSERT INTO answer (name, correct, question_id) VALUES ('Dziedziczenie (inheritance) w języku Java jest zastępowane przez kompozycję.', false, 15);

-- Pytanie 16
INSERT INTO answer (name, correct, question_id) VALUES ('Wyjątki (exceptions) w języku Java są specjalnymi obiektami, które reprezentują nieoczekiwane sytuacje podczas wykonywania programu i są obsługiwane za pomocą bloków try-catch.', true, 16);
INSERT INTO answer (name, correct, question_id) VALUES ('Wyjątki (exceptions) w języku Java są ignorowane i nie mają znaczenia.', false, 16);
INSERT INTO answer (name, correct, question_id) VALUES ('Wyjątki (exceptions) w języku Java mogą być używane tylko w metodach statycznych.', false, 16);
INSERT INTO answer (name, correct, question_id) VALUES ('Wyjątki (exceptions) w języku Java mogą być obsługiwane tylko przez rzucenie wyjątku.', false, 16);

-- Pytanie 17
INSERT INTO answer (name, correct, question_id) VALUES ('Jest to funkcja główna programu', true, 17);
INSERT INTO answer (name, correct, question_id) VALUES ('Jest to metoda instancyjna dowolnej klasy', false, 17);

-- Pytanie 18
INSERT INTO answer (name, correct, question_id) VALUES ('Metoda ta spowoduje przypisanie wartosci name', true, 18);
INSERT INTO answer (name, correct, question_id) VALUES ('Metoda ta spowoduje wyczyszczenie wartosci name', false, 18);


-- Dodanie pytań do quizu o ID 1
INSERT INTO question (name, file_url, quiz_id) VALUES
('Wykonanie poniższego programu spowoduje wyświetlenie:', NULL, 1),
('Które zapisy referencji do metody i wyrażenia Lambda są równoważne?', NULL, 1),
('Wykonanie poniższego programu spowoduje wyświetlenie:', NULL, 1),
('Głównym elementem pliku AndroidManifest.xml jest ?', NULL, 1),
('Które wyrażenie posortuje tablicę str dla aplikacji przedstawionej poniżej ?', NULL, 1),
('Klasa Activity platformy Java Android znajduje się w pakiecie ?', NULL, 1);

-- Dodanie odpowiedzi do pytania 1
INSERT INTO answer (name, correct, question_id) VALUES
('List:1List:2List:3', TRUE, 19),
('List:4List:5List:6', TRUE, 19),
('List:123456', FALSE, 19),
('error', FALSE, 19),
('List:{123456}', FALSE, 19);

-- Dodanie odpowiedzi do pytania 2
INSERT INTO answer (name, correct, question_id) VALUES
('System.out::println', TRUE, 20),
('x->System.out.println(x)', TRUE, 20),
('JButton::new', FALSE, 20),
('() -> new JButton()', TRUE, 20),
('int[]::new', TRUE, 20),
('x->new int[x]', TRUE, 20),
('String::valueOf', TRUE, 20),
('x -> String.valueOf(x)', TRUE, 20);

-- Dodanie odpowiedzi do pytania 3
INSERT INTO answer (name, correct, question_id) VALUES
('123', TRUE, 21),
('Programu nie można uruchomić, ponieważ nie da się skompilować', FALSE, 21),
('123.0', FALSE, 21),
('Program da się skompilować, a przy wykonaniu zgłosi wyjątek', FALSE, 21);

-- Dodanie odpowiedzi do pytania 4
INSERT INTO answer (name, correct, question_id) VALUES
('activity', FALSE, 22),
('manifest', FALSE, 22),
('application', TRUE, 22),
('android', FALSE, 22);

-- Dodanie odpowiedzi do pytania 5
INSERT INTO answer (name, correct, question_id) VALUES
('(s1,s2)->return (s1.length()-s2.length());', FALSE, 23),
('(s1,s2)->(s1.length()-s2.length());', TRUE, 23),
('(s1,s2)->return (s1.length()-s2.length());', FALSE, 23),
('(s1,s2)->(s1.length()-s2.length());', TRUE, 23);

-- Dodanie odpowiedzi do pytania 6
INSERT INTO answer (name, correct, question_id) VALUES
('android.activity', FALSE, 24),
('android.app', TRUE, 24),
('android.widget', FALSE, 24),
('android.application', FALSE, 24);
