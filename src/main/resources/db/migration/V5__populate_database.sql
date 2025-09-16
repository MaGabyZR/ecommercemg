INSERT INTO categories (name)
VALUES ('Produce'),        -- 1
       ('Dairy'),          -- 2
       ('Bakery'),         -- 3
       ('Meat & Seafood'), -- 4
       ('Beverages'),      -- 5
       ('Pantry'),         -- 6
       ('Frozen'),         -- 7
       ('Snacks'),         -- 8
       ('Household'),      -- 9
       ('Personal Care'); -- 10
INSERT INTO products (name, price, description, category_id)
VALUES ('Bananas (1 lb)', 0.59, 'Fresh yellow bananas, great for snacking or smoothies.', 1),
       ('Whole Milk (1 gallon)', 3.49, 'Grade A whole milk, vitamin D fortified.', 2),
       ('Sourdough Bread Loaf', 4.29, 'Artisan sourdough bread baked fresh daily.', 3),
       ('Boneless Chicken Breast (1 lb)', 5.99, 'Skinless, boneless chicken breast. Perfect for grilling or baking.',
        4),
       ('Coca-Cola (12-pack cans)', 7.99, '12-pack of 12 oz Coca-Cola classic cans.', 5),
       ('Peanut Butter (16 oz)', 2.99, 'Creamy peanut butter made from roasted peanuts.', 6),
       ('Frozen Pepperoni Pizza', 6.49, 'Thin crust pepperoni pizza ready to bake from frozen.', 7),
       ('Lays Classic Potato Chips (8 oz)', 3.79, 'Crispy and salty potato chips. A classic snack.', 8),
       ('Paper Towels (6 rolls)', 8.99, 'Super absorbent paper towels for household cleaning.', 9),
       ('Toothpaste (6 oz)', 2.49, 'Fluoride toothpaste for daily oral hygiene.', 10);
