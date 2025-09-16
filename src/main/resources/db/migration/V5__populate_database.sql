-- Inserting categories into the 'categories' table
INSERT INTO `categories` (`id`, `name`)
VALUES (1, 'Dairy & Eggs'),
       (2, 'Fruits & Vegetables'),
       (3, 'Bakery'),
       (4, 'Meat & Seafood'),
       (5, 'Pantry Staples'),
       (6, 'Beverages'),
       (7, 'Snacks');
-- Inserting 10 products into the 'products' table
INSERT INTO `products` (`name`, `price`, `description`, `category_id`)
VALUES ('Organic Whole Milk', 3.99, 'One gallon of fresh, USDA certified organic whole milk.', 1),
       ('Grade A Large Eggs', 2.79, 'A dozen large, grade A white eggs. Perfect for breakfast.', 1),
       ('Gala Apples', 4.50, 'A 3 lb bag of crisp and sweet Gala apples, great for snacking.', 2),
       ('Organic Bananas', 1.29, 'A bunch of fresh, organic bananas, sold by the pound.', 2),
       ('Whole Wheat Bread', 3.49, 'A 24 oz loaf of soft, 100% whole wheat bread.', 3),
       ('Boneless Chicken Breast', 9.99, 'Lean and tender boneless, skinless chicken breasts, family pack.', 4),
       ('Spaghetti Pasta', 1.99, 'A 16 oz box of classic spaghetti, perfect for your favorite Italian dishes.', 5),
       ('Extra Virgin Olive Oil', 8.75, 'A 500ml bottle of cold-pressed extra virgin olive oil.', 5),
       ('Dark Roast Coffee Beans', 12.50, 'A 12 oz bag of whole bean, fair-trade dark roast coffee.', 6),
       ('Kettle-Cooked Potato Chips', 4.25, 'A large bag of sea salt kettle-cooked potato chips for a crunchy snack.',
        7);