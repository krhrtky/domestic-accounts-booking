#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

const schemaPath = path.join(__dirname, '../interface/schema.graphql');
const outputDir = path.join(__dirname, '../backend/presentation/api/src/main/kotlin/graphql');

function parseGraphQLSchema(schemaContent) {
    const types = [];
    const queries = [];
    const mutations = [];
    
    const lines = schemaContent.split('\n');
    let currentType = null;
    let currentSection = null;
    
    for (const line of lines) {
        const trimmed = line.trim();
        
        if (trimmed.startsWith('type ') && !trimmed.includes('Query') && !trimmed.includes('Mutation')) {
            const typeName = trimmed.split(' ')[1];
            currentType = { name: typeName, fields: [] };
            currentSection = 'type';
        } else if (trimmed.startsWith('type Query')) {
            currentSection = 'query';
        } else if (trimmed.startsWith('type Mutation')) {
            currentSection = 'mutation';
        } else if (trimmed.includes(':') && !trimmed.startsWith('//') && !trimmed.startsWith('#')) {
            const [fieldName, fieldType] = trimmed.split(':').map(s => s.trim());
            
            if (currentSection === 'type' && currentType) {
                currentType.fields.push({ name: fieldName, type: fieldType });
            } else if (currentSection === 'query') {
                queries.push({ name: fieldName, type: fieldType });
            } else if (currentSection === 'mutation') {
                mutations.push({ name: fieldName, type: fieldType });
            }
        } else if (trimmed === '}' && currentType && currentSection === 'type') {
            types.push(currentType);
            currentType = null;
        }
    }
    
    return { types, queries, mutations };
}

function generateKotlinDataClass(type) {
    const fields = type.fields.map(field => {
        let kotlinType = convertGraphQLTypeToKotlin(field.type);
        return `    val ${field.name}: ${kotlinType}`;
    }).join(',\n');
    
    return `data class ${type.name}(
${fields}
)`;
}

function generateKotlinController(queries, mutations, types) {
    const queryMethods = queries.map(query => {
        const returnType = convertGraphQLTypeToKotlin(query.type);
        const params = extractParameters(query.type);
        const paramString = params.map(p => `${p.name}: ${convertGraphQLTypeToKotlin(p.type)}`).join(', ');
        
        return `    fun ${query.name}(${paramString}): ${returnType} {
        // TODO: Implement ${query.name}
        throw NotImplementedError("${query.name} not implemented")
    }`;
    }).join('\n\n');
    
    const mutationMethods = mutations.map(mutation => {
        const returnType = convertGraphQLTypeToKotlin(mutation.type);
        const params = extractParameters(mutation.type);
        const paramString = params.map(p => `${p.name}: ${convertGraphQLTypeToKotlin(p.type)}`).join(', ');
        
        return `    fun ${mutation.name}(${paramString}): ${returnType} {
        // TODO: Implement ${mutation.name}
        throw NotImplementedError("${mutation.name} not implemented")
    }`;
    }).join('\n\n');
    
    let content = `package com.example.api.graphql

import com.expediagroup.graphql.server.operations.Query
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.stereotype.Component

`;
    
    // データクラスを生成
    types.forEach(type => {
        content += `${generateKotlinDataClass(type)}\n\n`;
    });
    
    // Queryコントローラー
    if (queries.length > 0) {
        content += `@Component
class QueryController : Query {
${queryMethods}
}

`;
    }
    
    // Mutationコントローラー
    if (mutations.length > 0) {
        content += `@Component
class MutationController : Mutation {
${mutationMethods}
}`;
    }
    
    return content;
}

function convertGraphQLTypeToKotlin(graphqlType) {
    const type = graphqlType.replace(/[!\[\]]/g, '').trim();
    
    switch (type) {
        case 'ID':
        case 'String': return 'String';
        case 'Int': return 'Int';
        case 'Float': return 'Double';
        case 'Boolean': return 'Boolean';
        default: return type;
    }
}

function extractParameters(queryType) {
    const params = [];
    const match = queryType.match(/\(([^)]+)\)/);
    if (match) {
        const paramString = match[1];
        const paramPairs = paramString.split(',');
        for (const pair of paramPairs) {
            const [name, type] = pair.split(':').map(s => s.trim());
            params.push({ name, type });
        }
    }
    return params;
}

function main() {
    try {
        const schemaContent = fs.readFileSync(schemaPath, 'utf8');
        const parsed = parseGraphQLSchema(schemaContent);
        
        if (!fs.existsSync(outputDir)) {
            fs.mkdirSync(outputDir, { recursive: true });
        }
        
        const controllerContent = generateKotlinController(parsed.queries, parsed.mutations, parsed.types);
        const outputPath = path.join(outputDir, 'GeneratedControllers.kt');
        
        fs.writeFileSync(outputPath, controllerContent);
        
        console.log(`✅ Kotlinコントローラーが生成されました: ${outputPath}`);
        console.log(`生成されたタイプ: ${parsed.types.length}`);
        console.log(`生成されたクエリ: ${parsed.queries.length}`);
        console.log(`生成されたミューテーション: ${parsed.mutations.length}`);
        
    } catch (error) {
        console.error('❌ コントローラー生成中にエラーが発生しました:', error.message);
        process.exit(1);
    }
}

if (require.main === module) {
    main();
}