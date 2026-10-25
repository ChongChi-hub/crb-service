INSERT INTO meta_seo (entity_id, entity_type, meta_keywords)
SELECT 
    p.id,                     
    'POST',                
    p.keyword             
FROM 
    post p
WHERE 
    p.keyword IS NOT NULL; 