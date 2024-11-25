-- Создаем таблицу пользователей
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    login VARCHAR NOT NULL UNIQUE,
    role VARCHAR(3) NOT NULL CHECK (role IN ('ОП', 'Э', 'А')),
    fio VARCHAR,
    password VARCHAR NOT NULL CHECK (char_length(password) >= 8)
);

-- Создаем таблицу лекарственных растений
CREATE TABLE medicial_plant (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE,
    descript TEXT,
    gatherng_place VARCHAR,
    contrand VARCHAR NOT NULL,
    id_user INT NOT NULL
);

-- Создаем таблицу лекарственных сборов
CREATE TABLE medicial_collection (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE,
    count_plant INT, 
    id_user INT NOT NULL
);

-- Создаем таблицу состава сбора связь между расстением и сбором
CREATE TABLE composition_collect (
    id_plant INT NOT NULL,
    id_collection INT NOT NULL,
    time_gatherng TIME, 
    part_plant VARCHAR 
);

-- Создаем таблицу отзывов
CREATE TABLE reviews (
    id SERIAL PRIMARY KEY,
    id_collection INT NOT NULL,
    id_user INT NOT NULL,
    text_review TEXT,
    estimation INT NOT NULL CHECK (estimation BETWEEN 0 AND 10)
);

-- Создаем таблицу болезней
CREATE TABLE desease (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE
);

-- Создаем таблицу связи между болезнью и сбором 
CREATE TABLE method (
    id_desease INT NOT NULL,
    id_collection INT NOT NULL,
    metod_applic VARCHAR NOT NULL,
    realise_form VARCHAR,
    id_user INT NOT NULL
);

------------------------------------------------------------------------------------------
-- Создание связей
INSERT INTO users (id, login, password, role) VALUES (-1, 'deleted_user', '123456789', 'ОП');

ALTER TABLE medicial_plant
ALTER COLUMN id_user SET DEFAULT -1;
ALTER TABLE medicial_collection
ALTER COLUMN id_user SET DEFAULT -1;
ALTER TABLE reviews
ALTER COLUMN id_user SET DEFAULT -1;
ALTER TABLE method
ALTER COLUMN id_user SET DEFAULT -1;

ALTER TABLE medicial_plant
ADD CONSTRAINT fk_medicial_plant
FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE SET DEFAULT;

ALTER TABLE medicial_collection
ADD CONSTRAINT fk_medicial_collection
FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE SET DEFAULT;

ALTER TABLE reviews
ADD CONSTRAINT fk_reviews_to_user
FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE SET DEFAULT;

ALTER TABLE reviews
ADD CONSTRAINT fk_reviews_to_collection
FOREIGN KEY (id_collection) REFERENCES medicial_collection(id) ON DELETE CASCADE;

ALTER TABLE method
ADD CONSTRAINT fk_method_to_user
FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE SET DEFAULT;

ALTER TABLE method
ADD CONSTRAINT fk_method_to_desease
FOREIGN KEY (id_desease) REFERENCES desease(id) ON DELETE CASCADE;

ALTER TABLE method
ADD CONSTRAINT fk_method_to_collection
FOREIGN KEY (id_collection) REFERENCES medicial_collection(id) ON DELETE CASCADE;

ALTER TABLE composition_collect
ADD CONSTRAINT fk_composition_collect_to_plant
FOREIGN KEY (id_plant) REFERENCES medicial_plant(id) ON DELETE CASCADE;

ALTER TABLE composition_collect
ADD CONSTRAINT fk_composition_collect_to_collection
FOREIGN KEY (id_collection) REFERENCES medicial_collection(id) ON DELETE CASCADE;

ALTER TABLE method ADD COLUMN id SERIAL PRIMARY KEY;
ALTER TABLE composition_collect ADD COLUMN id SERIAL PRIMARY KEY;

----------------------------------------------------------------------------------------
-- триггеры для удалени сбора после удаления расстения или болезни
CREATE OR REPLACE FUNCTION delete_empty_collections()
RETURNS TRIGGER AS $$
BEGIN
    -- Проверяем, остались ли растения в сборе
    IF NOT EXISTS (
        SELECT 1 FROM composition_collect WHERE id_collection = OLD.id_collection
    ) THEN
        DELETE FROM medicial_collection WHERE id = OLD.id_collection;
    END IF;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_empty_collection
AFTER DELETE ON composition_collect
FOR EACH ROW
EXECUTE FUNCTION delete_empty_collections();

CREATE OR REPLACE FUNCTION delete_unused_collections()
RETURNS TRIGGER AS $$
BEGIN
    -- Проверяем, остались ли болезни, связанные с этим сбором
    IF NOT EXISTS (
        SELECT 1 FROM method WHERE id_collection = OLD.id_collection
    ) THEN
        DELETE FROM medicial_collection WHERE id = OLD.id_collection;
    END IF;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_unused_collection
AFTER DELETE ON method
FOR EACH ROW
EXECUTE FUNCTION delete_unused_collections();

----------------------------------------------------------------------------------------
-- функции дя вставки элементов
create or replace procedure insert_user(
    p_login VARCHAR,
    p_role VARCHAR(3),
    p_password VARCHAR,
    p_fio VARCHAR DEFAULT NULL
) language plpgsql as $$
begin
    if p_role not in ('ОП', 'Э', 'А') then 
        raise exception 'неверная роль';
    end if;
    if char_length(p_password) <= 8 then
        raise exception 'пароль должен быть минимум 8 символов';
    end if;
    if p_login in (select login from users where login = p_login) then
        raise exception 'пользователь с таким логином % уже существует', p_login;
    end if;
    insert into users (login, role, fio, password)
    values (p_login, p_role, p_fio, p_password);
end; $$ SECURITY INVOKER;

create or replace procedure insert_medcal_plant(
    p_name VARCHAR,
    p_contrand VARCHAR,
    p_id_user INT,
    p_descrpt TEXT DEFAULT NULL,
    p_gatherng_place VARCHAR DEFAULT NULL
) language plpgsql as $$
begin
    if p_id_user not in (select id from users where id = p_id_user) then
        raise exception 'пользователя с id % не существует', p_id_user;
    end if;
    if p_name in (select name from medicial_plant where name = p_name) then
        raise exception 'растение с таким названием % уже существует', p_name;
    end if;
    insert into medicial_plant (name, descript, gatherng_place, contrand, id_user)
    values (p_name, p_descrpt, p_gatherng_place, p_contrand, p_id_user);
end; $$ SECURITY INVOKER;

create or replace procedure insert_review(
    p_id_collection INT,
    p_id_user INT,
    p_estimation INT,
    p_text_review TEXT DEFAULT NULL
) language plpgsql as $$
begin
    if p_id_collection not in (select id from medicial_collection  where id = p_id_collection) then
        raise exception 'сбора с id % не существует', p_id_collection;
    end if;
    if p_id_user not in (select id from users where id = p_id_user) then
        raise exception 'пользователя с id % не существует', p_id_user;
    end if;
    insert into reviews (id_collection, id_user, estimation, text_review)
    values (p_id_collection, p_id_user, p_estimation, p_text_review);
end; $$ SECURITY INVOKER;

create or replace procedure insert_desease(
    p_name VARCHAR
) language plpgsql as $$
begin
    if p_name in (select name from desease where name = p_name) then
        raise exception 'болезнь с таким названием % уже существует', p_name;
    end if;
    insert into desease (name)
    values (p_name);
end; $$ SECURITY INVOKER;

create or replace procedure insert_medical_collection(
    p_name VARCHAR,
    p_id_user INT,
    p_id_desease INT,
    p_metod_applic VARCHAR,
    p_count_plant INT,
    p_plant_detail JSONB,
    p_realise_form VARCHAR DEFAULT NULL
) language plpgsql as $$
declare
    v_id_collection INT;
    plant_record JSONB;
begin
    if p_name in (select name from medicial_collection where name = p_name) then
        raise exception 'сбор с таким названием % уже существует', p_name;
    end if;
    if p_id_user not in (select id from users where id = p_id_user) then
        raise exception 'пользователя с id % не существует', p_id_user;
    end if;
    if p_id_desease not in (select id from desease where id = p_id_desease) then
        raise exception 'болезни с id % не существует', p_id_desease;
    end if;
    if jsonb_array_length(p_plant_detail) != p_count_plant then
        raise exception 'перданное количество информации о расстениях не соответсвует
            переданному количеству расстений';
    end if;

    -- вставляем сбор
    insert into medicial_collection (name, count_plant, id_user)
    values (p_name, p_count_plant, p_id_user)
    returning id into v_id_collection;

    -- вставляем связь сбора с болезнью
    insert into method (id_desease, id_collection, metod_applic, realise_form, id_user)
    values (p_id_desease, v_id_collection, p_metod_applic, p_realise_form, p_id_user);

    -- вставляем связь сбора с растениями
    for i in 0..p_count_plant - 1
    loop
        plant_record := p_plant_detail->i;
        if not exists (select id from medicial_plant where id = (plant_record->>'id_plant')::INT) then
            raise exception 'растения с id % не существует', plant_record->>'id_plant';
        end if;
        insert into composition_collect (id_plant, id_collection, time_gatherng, part_plant)
        values (
            (plant_record->>'id_plant')::INT,
            v_id_collection,
            COALESCE(NULLIF(plant_record->>'time_gatherng', '')::TIME, NULL),
            COALESCE(NULLIF(plant_record->>'part_plant', ''), NULL)
        );
    end loop;
end; $$ SECURITY INVOKER;

CREATE OR REPLACE PROCEDURE update_medical_collection(
    p_id_collection INT,
    p_name VARCHAR DEFAULT NULL,
    p_id_user INT DEFAULT NULL,
    p_id_desease INT DEFAULT NULL,
    p_metod_applic VARCHAR DEFAULT NULL,
    p_count_plant INT DEFAULT NULL,
    p_plant_detail JSONB DEFAULT NULL,
    p_realise_form VARCHAR DEFAULT NULL
) LANGUAGE plpgsql AS $$
DECLARE
    v_existing_name VARCHAR;
    v_existing_id_user INT;
    v_existing_id_desease INT;
    v_existing_metod_applic VARCHAR;
    v_existing_count_plant INT;
    v_existing_realise_form VARCHAR;
    plant_record JSONB;
BEGIN
    -- Проверка, существует ли сбор
    IF EXISTS (SELECT name FROM medicial_collection WHERE name = p_name) THEN
        RAISE EXCEPTION 'Сбор % существует', p_name;
    END IF;

    -- Получение текущих значений
    SELECT name, id_user, count_plant
    INTO v_existing_name, v_existing_id_user, v_existing_count_plant
    FROM medicial_collection
    WHERE id = p_id_collection;

    SELECT id_desease, metod_applic, realise_form
    INTO v_existing_id_desease, v_existing_metod_applic, v_existing_realise_form
    FROM method
    WHERE id_collection = p_id_collection;

    -- Обновление таблицы medicial_collection
    UPDATE medicial_collection
    SET
        name = COALESCE(NULLIF(p_name, ''), v_existing_name),
        id_user = COALESCE(p_id_user, v_existing_id_user),
        count_plant = COALESCE(p_count_plant, v_existing_count_plant)
    WHERE id = p_id_collection;

    -- Обновление таблицы method
    UPDATE method
    SET
        id_desease = COALESCE(p_id_desease, v_existing_id_desease),
        metod_applic = COALESCE(NULLIF(p_metod_applic, ''), v_existing_metod_applic),
        realise_form = COALESCE(NULLIF(p_realise_form, ''), v_existing_realise_form)
    WHERE id_collection = p_id_collection;

    -- Обновление информации о растениях, если p_plant_detail передан
    IF p_count_plant != 0 then
        -- Удаляем старые записи
        DELETE FROM composition_collect WHERE id_collection = p_id_collection;

        -- Проверяем, совпадает ли переданное количество растений с заявленным
        IF jsonb_array_length(p_plant_detail) != COALESCE(p_count_plant, v_existing_count_plant) THEN
            RAISE EXCEPTION 'Переданное количество информации о растениях не соответствует количеству растений';
        END IF;

        -- Вставляем новые записи о растениях
        FOR i IN 0..jsonb_array_length(p_plant_detail) - 1 LOOP
            plant_record := p_plant_detail->i;
            IF NOT EXISTS (SELECT 1 FROM medicial_plant WHERE id = (plant_record->>'id_plant')::INT) THEN
                RAISE EXCEPTION 'Растения с id % не существует', plant_record->>'id_plant';
            END IF;

            INSERT INTO composition_collect (id_plant, id_collection, time_gatherng, part_plant)
            VALUES (
                (plant_record->>'id_plant')::INT,
                p_id_collection,
                COALESCE(NULLIF(plant_record->>'time_gatherng', '')::TIME, NULL),
                COALESCE(NULLIF(plant_record->>'part_plant', ''), NULL)
            );
        END LOOP;
    END IF;
END;
$$ SECURITY INVOKER;

-- Роли
CREATE ROLE administrator WITH LOGIN;
GRANT USAGE ON SCHEMA public TO administrator;
CREATE ROLE expert WITH LOGIN;
GRANT USAGE ON SCHEMA public TO expert;
CREATE ROLE simpleuser WITH LOGIN;
GRANT USAGE ON SCHEMA public TO simpleuser;

GRANT SELECT, UPDATE, INSERT, DELETE ON ALL TABLES IN SCHEMA public TO administrator;
GRANT EXECUTE ON procedure insert_user(VARCHAR, VARCHAR(3), VARCHAR, VARCHAR) 
                            TO administrator;
GRANT EXECUTE ON procedure insert_medcal_plant(VARCHAR, VARCHAR, INT, TEXT, VARCHAR)
                            TO administrator;
GRANT EXECUTE ON procedure insert_review(INT, INT, INT, TEXT)
                            TO administrator;
GRANT EXECUTE ON procedure insert_desease(VARCHAR)
                            TO administrator;
GRANT EXECUTE ON procedure insert_medical_collection(VARCHAR, INT, INT, VARCHAR, 
                        INT, JSONB, VARCHAR) TO administrator;

GRANT INSERT, UPDATE, DELETE ON TABLE medicial_plant TO expert; 
GRANT INSERT, UPDATE, DELETE ON TABLE medicial_collection TO expert;
GRANT INSERT, UPDATE, DELETE ON TABLE composition_collect TO expert;
GRANT INSERT, UPDATE, DELETE ON TABLE method TO expert;
GRANT INSERT ON TABLE reviews TO expert;

GRANT SELECT ON ALL TABLES IN SCHEMA public TO expert;
GRANT EXECUTE ON procedure insert_medcal_plant(VARCHAR, VARCHAR, INT, 
                                                TEXT, VARCHAR) TO expert;
GRANT EXECUTE ON procedure insert_medical_collection(VARCHAR, INT, INT, VARCHAR, 
                                        INT, JSONB, VARCHAR) TO expert;
GRANT EXECUTE ON procedure insert_review(INT, INT, INT, TEXT) TO expert;

GRANT SELECT ON ALL TABLES IN SCHEMA public TO simpleuser;
GRANT INSERT ON TABLE reviews TO simpleuser;
GRANT EXECUTE ON procedure insert_review(INT, INT, INT, TEXT) TO simpleuser;

GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO public;

---------------------------------------------------------------------------------------------------------------
