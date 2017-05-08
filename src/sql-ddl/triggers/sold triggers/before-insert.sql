CREATE DEFINER=`root`@`localhost` TRIGGER `project`.`sold_BEFORE_INSERT` BEFORE INSERT ON `sold` FOR EACH ROW
BEGIN
	update book
    set book.quantity = book.quantity - NEW.quantity
    where book.ISBN = NEW.ISBN;
END