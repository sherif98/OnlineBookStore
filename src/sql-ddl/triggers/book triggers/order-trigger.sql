CREATE DEFINER = CURRENT_USER TRIGGER `project`.`book_AFTER_UPDATE` AFTER UPDATE ON `book` FOR EACH ROW
BEGIN
	if (NEW.quantity < NEW.threshold) then
		insert into book_order values(NEW.publisher_name, NEW.ISBN, NEW.threshold);
    end if;
END
