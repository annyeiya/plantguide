-- таблицы
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS medicial_plant CASCADE;
DROP TABLE IF EXISTS medicial_collection CASCADE;
DROP TABLE IF EXISTS composition_collect CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS desease CASCADE;
DROP TABLE IF EXISTS method CASCADE;

-- связи
ALTER TABLE medicial_plant
DROP CONSTRAINT IF EXISTS fk_medicial_plant;

ALTER TABLE medicial_collection
DROP CONSTRAINT IF EXISTS fk_medicial_collection;

ALTER TABLE reviews
DROP CONSTRAINT IF EXISTS fk_reviews_to_user;

ALTER TABLE reviews
DROP CONSTRAINT IF EXISTS fk_reviews_to_collection;

ALTER TABLE method
DROP CONSTRAINT IF EXISTS fk_method_to_user;

ALTER TABLE method
DROP CONSTRAINT IF EXISTS fk_method_to_desease;

ALTER TABLE method
DROP CONSTRAINT IF EXISTS fk_method_to_collection;

ALTER TABLE composition_collect
DROP CONSTRAINT IF EXISTS fk_composition_collect_to_plant;

ALTER TABLE composition_collect
DROP CONSTRAINT IF EXISTS fk_composition_collect_to_collection;

-- роли
RESET ROLE;
