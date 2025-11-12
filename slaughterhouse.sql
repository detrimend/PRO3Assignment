
set schema 'slaughterhouse';

-- Drop in dependency order (safe re-creation)
DROP TABLE IF EXISTS product_trays;
DROP TABLE IF EXISTS tray_parts;
DROP TABLE IF EXISTS parts;
DROP TABLE IF EXISTS trays;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS animals;


-- Animals
CREATE TABLE animals (
                         id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         weight DECIMAL(10,3) NOT NULL CHECK (weight > 0),
                         origin VARCHAR(100) DEFAULT 'Marx Pig Farm',
                         arrival_date varchar NOT NULL DEFAULT CURRENT_DATE
);

-- Parts (each Part belongs to an Animal)
CREATE TABLE parts (
                       id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       type VARCHAR(100) NOT NULL,
                       weight DECIMAL(10,3) NOT NULL CHECK (weight > 0),
                       animal_id INTEGER NOT NULL,
                       CONSTRAINT fk_parts_animal
                           FOREIGN KEY (animal_id) REFERENCES animals(id)
                               ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE INDEX idx_parts_animal ON parts(animal_id);


-- Trays (stack container for Parts)
CREATE TABLE trays (
                       id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       max_weight DECIMAL(10,3) NOT NULL CHECK (max_weight > 0),
                       current_weight DECIMAL(10,3) NOT NULL DEFAULT 0
                           CHECK (current_weight >= 0 AND current_weight <= max_weight)
);

-- Tray↔Part stack relation with position (top has highest position)
CREATE TABLE tray_parts (
                            tray_id INTEGER NOT NULL,
                            part_id INTEGER NOT NULL,
                            position INTEGER NOT NULL CHECK (position >= 0),
                            PRIMARY KEY (tray_id, position),
                            CONSTRAINT uq_tray_parts_part UNIQUE (part_id),
                            CONSTRAINT fk_tray_parts_tray
                                FOREIGN KEY (tray_id) REFERENCES trays(id)
                                    ON UPDATE CASCADE ON DELETE CASCADE,
                            CONSTRAINT fk_tray_parts_part
                                FOREIGN KEY (part_id) REFERENCES parts(id)
                                    ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE INDEX idx_tray_parts_part ON tray_parts(part_id);

-- Products
CREATE TABLE products (
                          id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          product_type VARCHAR(100) NOT NULL
);

-- Product.trayMap: Tray key with String value (label)
CREATE TABLE product_trays (
                               product_id INTEGER NOT NULL,
                               tray_id INTEGER NOT NULL,
                               label VARCHAR(255),
                               PRIMARY KEY (product_id, tray_id),
                               CONSTRAINT fk_product_trays_product
                                   FOREIGN KEY (product_id) REFERENCES products(id)
                                       ON UPDATE CASCADE ON DELETE CASCADE,
                               CONSTRAINT fk_product_trays_tray
                                   FOREIGN KEY (tray_id) REFERENCES trays(id)
                                       ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE INDEX idx_product_trays_tray ON product_trays(tray_id);

-- Dummy data: additional pigs, parts, trays, tray_parts, and products
-- Animals (ids 7..12)
INSERT INTO animals (id, weight)
    OVERRIDING SYSTEM VALUE
VALUES
    (7, 110.000),
    (8,  78.500),
    (9, 140.250),
    (10, 66.600),
    (11,130.000),
    (12, 92.750);

-- Parts (ids 14..30) - types repeat across animals
INSERT INTO parts (id, type, weight, animal_id)
    OVERRIDING SYSTEM VALUE
VALUES
    (14, 'Loin',        11.250, 7),
    (15, 'Belly',        9.000, 7),
    (16, 'Ham',         80.000, 8),
    (17, 'Shoulder',    14.500, 8),
    (18, 'Loin',        12.000, 9),
    (19, 'Belly',       10.500, 9),
    (20, 'Ham',         88.000, 10),
    (21, 'Picnic',      60.000, 10),
    (22, 'Jowl',        21.000, 11),
    (23, 'Rib rack',    75.250, 11),
    (24, 'Offal',        8.500, 12),
    (25, 'Kidney',       2.400, 12),
    (26, 'Liver',        3.600, 12),
    (27, 'Tongue',       1.300, 9),
    (28, 'Heart',        4.900, 8),
    (29, 'Loin',        11.800, 11),
    (30, 'Belly',        9.200, 10);

-- Trays (ids 5..8) with current_weight = sum of assigned parts
INSERT INTO trays (id, max_weight, current_weight)
    OVERRIDING SYSTEM VALUE
VALUES
    (5,  50.000, 21.550),   -- holds parts 14,15,27 => 11.250 + 9.000 + 1.300 = 21.550
    (6, 250.000,108.600),   -- holds parts 16,17,28,30 => 80.000 + 14.500 + 4.900 + 9.200 = 108.600
    (7, 200.000,182.300),   -- holds parts 18,19,20,21,29 => 12.000 + 10.500 + 88.000 + 60.000 + 11.800 = 182.300
    (8, 200.000,110.750);   -- holds parts 22,23,24,25,26 => 21.000 + 75.250 + 8.500 + 2.400 + 3.600 = 110.750

-- Tray parts (positions: higher = top)
INSERT INTO tray_parts (tray_id, part_id, position) VALUES
                                                        -- Tray 5
                                                        (5, 14, 0),
                                                        (5, 15, 1),
                                                        (5, 27, 2),

                                                        -- Tray 6
                                                        (6, 16, 0),
                                                        (6, 17, 1),
                                                        (6, 28, 2),
                                                        (6, 30, 3),

                                                        -- Tray 7
                                                        (7, 18, 0),
                                                        (7, 19, 1),
                                                        (7, 20, 2),
                                                        (7, 21, 3),
                                                        (7, 29, 4),

                                                        -- Tray 8
                                                        (8, 22, 0),
                                                        (8, 23, 1),
                                                        (8, 24, 2),
                                                        (8, 25, 3),
                                                        (8, 26, 4);

-- Products (ids 3..5) - store-ready products
INSERT INTO products (id, product_type)
    OVERRIDING SYSTEM VALUE
VALUES
    (3, 'Store Ready Sampler'),
    (4, 'BBQ Family Pack'),
    (5, 'Offal Essentials');

-- Map trays to products with store labels
INSERT INTO product_trays (product_id, tray_id, label) VALUES
                                                           (3, 5, 'Sampler Pack - assorted small cuts'),
                                                           (3, 8, 'Store Offal Mix'),
                                                           (4, 6, 'BBQ Ready Pack'),
                                                           (5, 7, 'Family Ham Pack');
SELECT * FROM parts WHERE animal_id = 7;
