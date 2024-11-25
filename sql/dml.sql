call insert_user('admin', 'А', '123456789');
call insert_user('expert', 'Э', 'qwertyuio');
call insert_user('anya', 'ОП', 'парольпароль');

call insert_desease('кашель');

call insert_medcal_plant('ромашка', 'нет', 2, 'обычная ромашка', 'в горах');
call insert_medcal_plant('корень солодки', 'нет', 2, 'обычный корень солодки', 'в степях');

call insert_medical_collection('сбор от кашля', 2, 1, 'пить 2 раза в день', 2, 
	'[{"id_plant": 1, "time_gatherng": "12:00", "part_plant": "сок"},
    	{"id_plant": 2, "part_plant": "корень"}]'::jsonb, 'в виде сбора'
	);

call insert_review(1, 3, 9, 'помогло');
call insert_review(1, 2, 9, 'сам лечусь только этим!');