alter table votes_on_answers alter column vote type varchar(255)
    using case when vote = 1 then 'UP' when vote = -1 then 'DOWN' end notnull;
alter table votes_on_answers rename column vote to vote_type;