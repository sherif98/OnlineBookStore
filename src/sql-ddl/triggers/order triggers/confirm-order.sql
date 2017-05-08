CREATE DEFINER = CURRENT_USER TRIGGER `project`.`book_order_BEFORE_DELETE` BEFORE DELETE ON `book_order` FOR EACH ROW
BEGIN
	update book
    set book.quantity = book.quantity + OLD.quantity
    where book.ISBN = OLD.ISBN;
END
